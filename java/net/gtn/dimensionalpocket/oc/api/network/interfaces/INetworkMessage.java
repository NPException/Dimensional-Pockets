package net.gtn.dimensionalpocket.oc.api.network.interfaces;

import net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.MessageResponse;

/**
 * Used to solely define a message.
 * Gives a proper interface for the system to pass through simply.
 * <p/>
 * The basic idea of a message is the container of information.
 * Once posted the message will be run through the processing system delivering it to all nearby nodes connected.
 * This implementation can vary.
 * For instance, in {@link net.gtn.dimensionalpocket.oc.api.network.NetworkCore}, I run it through a breadth-first search, resulting in a propagation.
 * <p/>
 */
public interface INetworkMessage {

    /**
     * Should be set upon creation.
     *
     * @param node the message being passed ownership.
     */
    public void setOwner(INetworkNode node);

    /**
     * @return current INetworkNode that is viewed as the node that sent out this message.
     */
    public INetworkNode getOwner();

    /**
     * Called when the message gets reposted upon completion and requires reposting.
     * <p/>
     * Use this to reset any values (if you want to reset anything) before the repost.
     */
    public void resetMessage();

    /**
     * The contents of this message were changed in some way.
     *
     * @param node The node that returned the INJECT state.
     */
    public void onDataChanged(INetworkNode node);

    /**
     * Fired during the preProcessing Phase.
     * Can be used to setup/(wait for) data.
     * Such as a path to a node, or waiting for another messages execution.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @return Take a look at {@link net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse preProcessing(IMessageProcessor messageProcessor);

    /**
     * Fired during the processing Phase.
     *
     * If the message wants to add it to a list, or alter something, you have the ability to.
     * A node will not be fired with this method more than once.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @param node - A node that exists as a part of the network.
     * @return Take a look at {@link net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse processNode(IMessageProcessor messageProcessor, INetworkNode node);

    /**
     * Fired during the postProcessing Phase.
     *
     * Used to determine what the system should do with the message after giving passing it off to this method.
     * This is after the message has been completed.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @return Take a look at {@link net.gtn.dimensionalpocket.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse postProcessing(IMessageProcessor messageProcessor);

}
