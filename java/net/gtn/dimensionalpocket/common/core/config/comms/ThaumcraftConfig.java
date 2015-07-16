package net.gtn.dimensionalpocket.common.core.config.comms;

import net.gtn.dimensionalpocket.common.core.config.comms.framework.AbstractConfig;
import net.gtn.dimensionalpocket.common.lib.Reference;


public class ThaumcraftConfig extends AbstractConfig {

	public ThaumcraftConfig(String modID) {
		super(modID);
	}

	@Override
	public void runModSpecificComms() {
		// do nothing
	}

	@Override
	public void sendInterModComms() {
		sendInterComms("dimensionBlacklist", Reference.DIMENSION_ID + ":0");
		sendInterComms("biomeBlacklist", Reference.BIOME_ID + ":0");
	}
}
