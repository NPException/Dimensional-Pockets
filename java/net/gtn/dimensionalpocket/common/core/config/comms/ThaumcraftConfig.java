package net.gtn.dimensionalpocket.common.core.config.comms;

import net.gtn.dimensionalpocket.common.core.BiomeHelper;
import net.gtn.dimensionalpocket.common.core.config.comms.framework.AbstractConfig;
import net.gtn.dimensionalpocket.common.lib.Reference;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThaumcraftConfig extends AbstractConfig {

    public ThaumcraftConfig(String modID) {
        super(modID);
    }

    @Override
    public void runModSpecificComms() {

    }

    @Override
    public void sendInterModComms() {
        sendInterComms("dimensionBlacklist", Reference.DIMENSION_ID + ":0");
        sendInterComms("biomeBlacklist", BiomeHelper.BIOME_ID + ":0");
    }
}
