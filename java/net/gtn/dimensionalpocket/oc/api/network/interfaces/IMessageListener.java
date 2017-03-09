package net.gtn.dimensionalpocket.oc.api.network.interfaces;

import static net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.ListenerResponse;

public interface IMessageListener extends INetworkNode {

    /**
     * Used to determine what types of messages this node should be notified of.
     * Can be used to be notified of multiple message types.
     *
     * @return null or empty array for all messages.
     */
    public String[] getListenerType();

    /**
     * If you wish to override a message being posted, if returned with
     * - IGNORE, should be a default response, means you don't wish for anything to change.
     * - DELETE, the process will stop there, and the system will no longer do anything with that message.
     * - INTERCEPT, It will give you full control over the message, and label this node as the handler. It will not be removed from the queue.
     * - NOTE: You don't need to set the owner to this node. It's done after you return INTERCEPT;
     * - INJECT, This is to signify that data was added/removed/changed in some way.
     *
     * @param message The message in question that was posted.
     */
    public ListenerResponse onMessagePosted(INetworkMessage message);


}
