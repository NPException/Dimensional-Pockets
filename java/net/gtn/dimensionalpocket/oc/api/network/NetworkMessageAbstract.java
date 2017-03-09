package net.gtn.dimensionalpocket.oc.api.network;

import net.gtn.dimensionalpocket.oc.api.network.interfaces.INetworkMessage;
import net.gtn.dimensionalpocket.oc.api.network.interfaces.INetworkNode;

/**
 * Just a simple abstract class to remove the monotony of setting and getting owners
 */
public abstract class NetworkMessageAbstract implements INetworkMessage {

    protected INetworkNode owner;

    public NetworkMessageAbstract(INetworkNode owner) {
        this.owner = owner;
    }

    @Override
    public void setOwner(INetworkNode owner) {
        this.owner = owner;
    }

    @Override
    public INetworkNode getOwner() {
        return owner;
    }

}
