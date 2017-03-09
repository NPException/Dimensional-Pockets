package net.gtn.dimensionalpocket.oc.api.network.interfaces;

import java.util.Collection;
import java.util.Map;

/**
 * Yes, IMessageProcessor and INetworkNodeHandler should be implemented on the same object.
 * Moving the messageProcessor to another class is just going to result in more object references being thrown to and fro classes.
 * It's cleaner.
 * <p/>
 * If you think that my restriction is stupid, or I'm stupid, or other words that I can't be bothered thinking of,
 * Find me on #minecraftforge IRC, and tell me to change it to allow other classes to be the messageProcessor.
 */
public interface INetworkNodeHandler {

    /**
     * @param node Node being added to the network
     * @return true if the node was added.
     */
    public boolean addNetworkNode(INetworkNode node);

    /**
     * @param node Node being removed from the network
     * @return true if the node was removed.
     */
    public boolean removeNetworkNode(INetworkNode node);

    /**
     * @param nodes Collection of the nodes to retain.
     * @return true if the data structure was modified.
     */
    public boolean retainAll(Collection<? extends INetworkNode> nodes);

    /**
     * Called by the main NetworkInstance to merge two or more networks into a master network, in the event that a node is placed between two networks.
     * A map of the nodes and they're connections.
     * Think of this as an addAll for a data structure.
     *
     * @param networkNodeMap the adjacency map
     */
    public void mergeNetwork(Map<? extends INetworkNode, ? extends Collection<INetworkNode>> networkNodeMap);

    /**
     * @return A map of all nodes and their connections. (the adjacency map)
     */
    public Map<? extends INetworkNode, ? extends Collection<INetworkNode>> getNodeMap();

    /**
     * @param node the node in question
     * @return true if the network contains the node in question
     */
    public boolean containsNode(INetworkNode node);

    /**
     * If you wish for this class to be registered with:
     * FMLCommonHandler.instance().bus().register(networkCore);
     * Upon creation.
     * <p/>
     * It will be unregistered if the instance is no longer required.
     */
    public boolean requiresRegistration();

    /**
     * @return number of nodes in the network.
     */
    public int size();

    /**
     * @return true if there is no nodes currently in the network. (size() == 0;)
     */
    public boolean isEmpty();

    /**
     * Called when the instance is about to be deleted.
     * To be safe, it's probably best to clear any data structures and messaging systems that are running, so that garbage collection can grab it.
     */
    public void destroy();

}
