package net.gtn.dimensionalpocket.common.core.config.comms.framework;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public abstract class AbstractConfig implements IInterModConfig {

    private String modID;

    public AbstractConfig(String modID) {
        this.modID = modID;
    }

    public void sendInterComms(String key, String value) {
        FMLInterModComms.sendMessage(modID, key, value);
    }

    @Override
    public String getModID() {
        return modID;
    }
}
