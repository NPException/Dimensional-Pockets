package net.gtn.dimensionalpocket.oc.api.network;

import cpw.mods.fml.common.FMLCommonHandler;
import net.gtn.dimensionalpocket.oc.api.network.exceptions.NetworkAddException;
import net.gtn.dimensionalpocket.oc.api.network.exceptions.NetworkCreationException;
import net.gtn.dimensionalpocket.oc.api.network.exceptions.NetworkException;
import net.gtn.dimensionalpocket.oc.api.network.exceptions.NetworkRemoveException;
import net.gtn.dimensionalpocket.oc.api.network.interfaces.INetworkNode;
import net.gtn.dimensionalpocket.oc.api.network.interfaces.INetworkNodeHandler;

import java.util.*;

import static net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.*;

/**
 * This is the master instance.
 * Used to add and remove nodes to your network.
 */
public class NetworkInstance {

    private ArrayList<INetworkNodeHandler> networks;
    private Class<? extends INetworkNodeHandler> nodeHandlerClazz;

    public NetworkInstance() {
        this(NetworkCore.class);
    }

    public NetworkInstance(Class<? extends INetworkNodeHandler> nodeHandlerClazz) {
        this.networks = new ArrayList<>();
        this.nodeHandlerClazz = nodeHandlerClazz;
    }

    public NodeAdded addNetworkNode(INetworkNode node) throws NetworkException {
        List<INetworkNodeHandler> networksFound = new ArrayList<>();
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();

        if (!nearbyNodes.isEmpty())
            nodeIterator:
                    for (INetworkNode nearbyNode : nearbyNodes)
                        for (INetworkNodeHandler networkFound : networks)
                            if (!networksFound.contains(networkFound))
                                if (networkFound.containsNode(nearbyNode)) {
                                    networksFound.add(networkFound);
                                    continue nodeIterator;
                                }

        INetworkNodeHandler networkNodeHandler;
        NodeAdded response;
        switch (networksFound.size()) {
            case 0:
                response = NodeAdded.NETWORK_CREATION;
                networkNodeHandler = createNodeHandler();
                break;
            case 1:
                response = NodeAdded.NETWORK_JOIN;
                networkNodeHandler = networksFound.get(0);
                break;
            default:
                response = NodeAdded.NETWORK_MERGE;
                networkNodeHandler = networksFound.get(0);
                List<INetworkNodeHandler> nodes = networksFound.subList(1, networksFound.size());
                for (INetworkNodeHandler networkFound : nodes) {
                    networkNodeHandler.mergeNetwork(networkFound.getNodeMap());
                    removeNetworkNodeHandler(networkFound);
                }
        }

        boolean flag = networkNodeHandler.addNetworkNode(node);
        if (!flag) {
            if (response == NodeAdded.NETWORK_CREATION)
                removeNetworkNodeHandler(networkNodeHandler);

            throw new NetworkAddException("Failed to add node to network. Node: %s", node.getClass());
        }
        return response;
    }

    public NodeRemoved removeNetworkNode(INetworkNode node) throws NetworkException {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks) {
            if (networkNodeHandler.containsNode(node)) {
                nodeHandler = networkNodeHandler;
                break;
            }
        }

        if (nodeHandler == null)
            return NodeRemoved.NETWORK_NOT_FOUND;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();

        boolean removed = nodeHandler.removeNetworkNode(node);
        if (!removed)
            throw new NetworkRemoveException("Failed to remove node from network. Node: %s", node.getClass());

        Collection<INetworkNode> nearbyNodes = nodeMap.get(node);
        switch (nearbyNodes.size()) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
                return NodeRemoved.NETWORK_DESTROYED;
            case 1:
                return NodeRemoved.NETWORK_LEAVE;
            default:
                Iterator<INetworkNode> nextNode = nearbyNodes.iterator();

                INetworkNode networkNode = nextNode.next();
                Collection<INetworkNode> connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);

                int nodeHandlerSize = nodeHandler.size();
                if (connectedNodes.size() == nodeHandlerSize)
                    return NodeRemoved.NETWORK_LEAVE;

                HashSet<INetworkNode> visited = new HashSet<>(connectedNodes);
                visited.add(node);
                nodeHandler.retainAll(connectedNodes);

                Set<? extends INetworkNode> keySet = nodeMap.keySet();
                while (visited.size() != keySet.size()) {
                    if (!nextNode.hasNext())
                        break;
                    networkNode = nextNode.next();
                    if (visited.contains(networkNode))
                        continue;
                    connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);
                    visited.addAll(connectedNodes);
                    createNodeHandlerAndFill(connectedNodes);
                }
        }

        return NodeRemoved.NETWORK_SPLIT;
    }

    public NodeUpdated updateNetworkNode(INetworkNode node) throws NetworkException {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks)
            if (networkNodeHandler.containsNode(node)) {
                nodeHandler = networkNodeHandler;
                break;
            }

        if (nodeHandler == null)
            return NodeUpdated.NETWORK_NOT_FOUND;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();
        Collection<INetworkNode> cachedNodes = nodeMap.get(node);
        Collection<INetworkNode> currentNodes = node.getNearbyNodes();

        if (cachedNodes.equals(currentNodes))
            return NodeUpdated.NETWORK_NO_DELTA_DETECTED;

        boolean removed = nodeHandler.removeNetworkNode(node);
        if (!removed)
            throw new NetworkRemoveException("Failed to remove node from network. Node: %s", node.getClass());

        Collection<INetworkNode> nearbyNodes = nodeMap.get(node);
        switch (nearbyNodes.size()) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
            case 1:
                break;
            default:
                Iterator<INetworkNode> nextNode = nearbyNodes.iterator();

                INetworkNode networkNode = nextNode.next();
                Collection<INetworkNode> connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);

                int nodeHandlerSize = nodeHandler.size();
                if (connectedNodes.size() == nodeHandlerSize)
                    break;

                HashSet<INetworkNode> visited = new HashSet<>(connectedNodes);
                visited.add(node);
                nodeHandler.retainAll(connectedNodes);

                Set<? extends INetworkNode> keySet = nodeMap.keySet();
                while (visited.size() != keySet.size()) {
                    if (!nextNode.hasNext())
                        break;
                    networkNode = nextNode.next();
                    if (visited.contains(networkNode))
                        continue;
                    connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);
                    visited.addAll(connectedNodes);
                    createNodeHandlerAndFill(connectedNodes);
                }
        }

        addNetworkNode(node);
        return NodeUpdated.NETWORK_UPDATED;
    }

    private INetworkNodeHandler createNodeHandler() throws NetworkCreationException {
        INetworkNodeHandler nodeHandler = null;
        try {
            nodeHandler = nodeHandlerClazz.newInstance();
        } catch (Exception e) {
            throw new NetworkCreationException(e, "Failed to created INetworkNodeHandler. Class %s", nodeHandlerClazz);
        }
        if (nodeHandler.requiresRegistration())
            FMLCommonHandler.instance().bus().register(nodeHandler);
        networks.add(nodeHandler);
        return nodeHandler;
    }

    private INetworkNodeHandler createNodeHandlerAndFill(Collection<INetworkNode> collection) throws NetworkCreationException {
        INetworkNodeHandler nodeHandler = createNodeHandler();
        for (INetworkNode node : collection)
            nodeHandler.addNetworkNode(node);
        return nodeHandler;
    }

    private void removeNetworkNodeHandler(INetworkNodeHandler networkCore) {
        networkCore.destroy();
        if (networkCore.requiresRegistration())
            FMLCommonHandler.instance().bus().unregister(networkCore);
        networks.remove(networkCore);
    }

    /**
     * @param startingNode - Starting node.
     * @return A collection of all connected nodes.
     */
    public Collection<INetworkNode> breadthFirstSearchSpread(INetworkNode startingNode, Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap) {
        HashSet<INetworkNode> visited = new HashSet<>();
        Queue<INetworkNode> queue = new LinkedList<>();
        queue.offer(startingNode);

        while (!queue.isEmpty()) {
            INetworkNode node = queue.poll();
            visited.add(node);
            Collection<INetworkNode> nearbyNodes = nodeMap.get(node);
            for (INetworkNode childNode : nearbyNodes) {
                if (visited.contains(childNode))
                    continue;
                queue.offer(childNode);
            }
        }
        return visited;
    }
}
