package net.gtn.dimensionalpocket.oc.api.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gtn.dimensionalpocket.oc.api.collect.Graph;
import net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.MessageResponse;
import net.gtn.dimensionalpocket.oc.api.network.interfaces.*;
import net.gtn.dimensionalpocket.oc.api.network.search.SearchThread;

import java.util.*;

import static net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.ListenerResponse;

public class NetworkCore implements INetworkNodeHandler, IMessageProcessor {

    /**
     * Backing data structure for the network.
     * This maintains links/connections between nodes.
     * Something of my own design, will probably change through time.
     */
    private Graph<INetworkNode> graph;

    /**
     * Messages that are posted to the network are placed in this map.
     * Each server tick they are promoted through {@link NetworkCore.Phase}
     * <p/>
     * O(1) across the board for a lot of actions.
     * Fastest, but not necessarily the best.
     */
    private EnumMap<Phase, Collection<INetworkMessage>> messageMap;

    /**
     * Used to keep track of what nodes would like to be notified of messages being posted to the system.
     * Useful if you wish to have a "brain" of the network.
     */
    private ArrayList<IMessageListener> messageListeners;

    public NetworkCore() {
        graph = new Graph<>();
        messageListeners = new ArrayList<>();

        messageMap = new EnumMap<>(Phase.class);
        messageMap.put(Phase.PRE_PROCESSING, new ArrayList<INetworkMessage>());
        messageMap.put(Phase.PROCESSING, new ArrayList<INetworkMessage>());
        messageMap.put(Phase.POST_PROCESSING, new ArrayList<INetworkMessage>());
    }

    @Override
    public boolean addNetworkNode(INetworkNode node) {
        boolean flag = graph.addNode(node);
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();
        if (nearbyNodes == null)
            throw new RuntimeException(node.getType() + " returned a null set of nodes!");
        if (!nearbyNodes.isEmpty())
            for (INetworkNode nearbyNode : nearbyNodes)
                graph.addEdge(node, nearbyNode);
        node.setIMessageProcessor(this);
        if (node instanceof IMessageListener)
            messageListeners.add((IMessageListener) node);
        return flag;
    }

    @Override
    public boolean removeNetworkNode(INetworkNode node) {
        if (node instanceof IMessageListener)
            messageListeners.remove(node);
        return graph.removeNode(node);
    }

    @Override
    public boolean retainAll(Collection<? extends INetworkNode> nodes) {
        return graph.retainAll(nodes);
    }

    @Override
    public void mergeNetwork(Map<? extends INetworkNode, ? extends Collection<INetworkNode>> networkNodeMap) {
        graph.addAll(networkNodeMap);
        for (INetworkNode node : graph.getNodes())
            node.setIMessageProcessor(this);
    }

    @Override
    public Map<? extends INetworkNode, ? extends Collection<INetworkNode>> getNodeMap() {
        return graph.asMap();
    }

    @Override
    public boolean requiresRegistration() {
        return true;
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public void destroy() {
        graph.clear();
        messageMap.clear();
        messageListeners.clear();
    }

    @Override
    public boolean containsNode(INetworkNode node) {
        return graph.containsNode(node);
    }

    @Override
    public boolean postMessage(INetworkMessage message) {
        if (message.getOwner() == null)
            return false;
        
        if (!messageListeners.isEmpty()) {
            for (IMessageListener node : messageListeners) {
                ListenerResponse response = node.onMessagePosted(message);

                if (response == null)
                    throw new RuntimeException(node.getClass() + " returned a null response from onMessagePosted()");

                switch (response) {
                    case DELETE:
                        return false;
                    case INTERCEPT:
                        message.setOwner(node);
                    case INJECT:
                        message.onDataChanged(node);
                    default:
                    case IGNORE:
                }
            }
        }

        Collection<INetworkMessage> messages = messageMap.get(Phase.PRE_PROCESSING);
        messages.add(message);
        return messages.contains(message);
    }

    /**
     * Don't even ask...
     * This is going to be rewritten or destroyed...
     */
    @Override
    public ISearchResult getPathFrom(INetworkNode startNode, INetworkNode endNode) {
        if (!(graph.containsNode(startNode) && graph.containsNode(endNode)))
            return SearchThread.EMPTY_SEARCH;
        return SearchThread.addSearchPattern(startNode, endNode, getNodeMap());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        processingPostMessages();
        processingMessages();
        processingPreMessages();
    }

    private void processingPostMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.POST_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            MessageResponse response = message.postProcessing(this);

            if (response == null)
                throw new RuntimeException(message.getClass() + " returned a null response from postProcessing()");

            switch (response) {
                case INVALID:
                    iterator.remove();
                    message.resetMessage();
                    messageMap.get(Phase.PRE_PROCESSING).add(message);
                    continue messageIterator;
                case WAIT:
                    break;
                default:
                case VALID:
                    iterator.remove();
            }
        }
    }

    private void processingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            HashSet<INetworkNode> visited = new HashSet<>(graph.size());
            Queue<INetworkNode> queue = new ArrayDeque<>(graph.size());
            queue.offer(message.getOwner());

            while (!queue.isEmpty()) {
                INetworkNode node = queue.poll();
                visited.add(node);

                MessageResponse response = message.processNode(this, node);

                if (response == null)
                    throw new RuntimeException(message.getClass() + " returned a null response from processNode()");

                switch (response) {
                    case VALID:
                        for (INetworkNode childNode : graph.adjacentTo(node))
                            if (!visited.contains(childNode)) {
                                if (childNode.getType().equals(message.getOwner().getType()))
                                    queue.offer(childNode);
                            }
                        break;
                    case WAIT:
                        continue messageIterator;
                    case INVALID:
                }
            }

            iterator.remove();
            messageMap.get(Phase.POST_PROCESSING).add(message);
        }
    }

    private void processingPreMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PRE_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            MessageResponse response = message.preProcessing(this);

            if (response == null)
                throw new RuntimeException(message.getClass() + " returned a null response from preProcessing()");

            switch (response) {
                case INVALID:
                    iterator.remove();
                    break;
                case WAIT:
                    continue;
                case VALID:
            }

            iterator.remove();
            messageMap.get(Phase.PROCESSING).add(message);
        }
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    private static enum Phase {
        /**
         * Messages being posted, this is the stage where intercepts would happen.
         */
        PRE_PROCESSING,
        /**
         * The main phase of the message.
         * What happens during this phase of the message is up to the implementation.
         */
        PROCESSING,
        /**
         * The final stage of messages.
         * Finalising all the message properties to allow the message to act one last time before deletion.
         */
        POST_PROCESSING;
    }
}
