package net.gtn.dimensionalpocket.common.core.config;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.common.core.config.comms.ThaumcraftConfig;
import net.gtn.dimensionalpocket.common.core.config.comms.TinkersConstructConfig;
import net.gtn.dimensionalpocket.common.core.config.comms.framework.IInterModConfig;
import cpw.mods.fml.common.Loader;


public class InterModConfigHandler {

	private static ArrayList<IInterModConfig> configList = new ArrayList<>();

	public static void initComms() {
		configList.add(new ThaumcraftConfig("Thaumcraft"));
		configList.add(new TinkersConstructConfig("TConstruct"));

		runComms();
	}

	private static void runComms() {
		for (IInterModConfig config : configList) {
			if (Loader.isModLoaded(config.getModID())) {
				config.runModSpecificComms();
				config.sendInterModComms();
			}
		}
	}
}
