package net.gtn.dimensionalpocket.oc.api.network.interfaces;

/**
 * Just an interface the nodes can use to harmlessly post messages through to their current network's message system.
 */
public interface IMessageProcessor {

    /**
     * Method used to post said messages.
     * This removes the need for the node to have a direct reference to the {@link net.gtn.dimensionalpocket.oc.api.network.interfaces.INetworkNodeHandler}
     * This can remove a lot of stupid errors when someone has direct access to the object.
     * @param message The message being posted.
     */
    public boolean postMessage(INetworkMessage message);

    /**
     * If no path was found between pointA(from) and pointB(to) then an empty list will be returned.
     * The exact method of search is at the liberty of the implementation.
     * The recommended method would be Breadth First Search. (BFS)
     *
     * @param from Starting node
     * @param to Finishing node
     * @return An ordered list starting at: from, and follows a path to: to
     */
    public ISearchResult getPathFrom(INetworkNode from, INetworkNode to);

}
