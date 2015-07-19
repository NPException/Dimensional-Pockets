/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import de.npe.gameanalytics.SimpleAnalytics;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends SimpleAnalytics {

	public static final DPAnalytics analytics = new DPAnalytics();

	private static String MAIN_POCKET = "Pocket:";

	private static String SUB_PLAYER = "Player:";
	private static String SUB_TRANSFER = "Transfer:";

	private static String TELEPORT = "Teleport";
	private static String TRAPPED = "Trapped";
	private static String ITEMS = "Items";
	private static String ENERGY_RF = "EnergyRF";
	private static String FLUIDS = "Fluids";

	public static String ANALYITCS_PLAYER_TELEPORT = MAIN_POCKET + SUB_PLAYER + TELEPORT;
	public static String ANALYITCS_PLAYER_TRAPPED = MAIN_POCKET + SUB_PLAYER + TRAPPED;
	public static String ANALYITCS_TRANSFER_ITEMS = MAIN_POCKET + SUB_TRANSFER + ITEMS;
	public static String ANALYITCS_TRANSFER_ENERGY_RF = MAIN_POCKET + SUB_TRANSFER + ENERGY_RF;
	public static String ANALYITCS_TRANSFER_FLUIDS = MAIN_POCKET + SUB_TRANSFER + FLUIDS;


	public DPAnalytics() {
		super(Reference.VERSION, Reference.GA_GAME_KEY, Reference.GA_SECRET_KEY);
	}

	@Override
	public boolean isActive() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA &&
				(isClient() ? Minecraft.getMinecraft().isSnooperEnabled() : MinecraftServer.getServer().isSnooperEnabled());
	}
}
