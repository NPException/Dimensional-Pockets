package net.gtn.dimensionalpocket.oc.client;

import net.gtn.dimensionalpocket.oc.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

}
