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
	private static String SUB_STATE = "State:";
	private static String SUB_CRAFTED = "Crafted:";

	public static String ANALYITCS_PLAYER_TELEPORT = MAIN_POCKET + SUB_PLAYER + "Teleport";
	public static String ANALYITCS_PLAYER_TRAPPED = MAIN_POCKET + SUB_PLAYER + "Trapped";
	public static String ANALYITCS_TRANSFER_ITEMS = MAIN_POCKET + SUB_TRANSFER + "Items";
	public static String ANALYITCS_TRANSFER_ENERGY_RF = MAIN_POCKET + SUB_TRANSFER + "EnergyRF";
	public static String ANALYITCS_TRANSFER_FLUIDS = MAIN_POCKET + SUB_TRANSFER + "Fluids";
	public static String ANALYTICS_POCKET_PLACED = MAIN_POCKET + SUB_STATE + "Placed";
	public static String ANALYTICS_POCKET_MINED = MAIN_POCKET + SUB_STATE + "Mined";
	public static String ANALYTICS_POCKET_CRAFTED_PLAYER = MAIN_POCKET + SUB_STATE + SUB_CRAFTED + "Player";
	public static String ANALYTICS_POCKET_CRAFTED_AUTOMATION = MAIN_POCKET + SUB_STATE + SUB_CRAFTED + "Automation";


	public DPAnalytics() {
		super(Reference.VERSION, Reference.GA_GAME_KEY, Reference.GA_SECRET_KEY);
	}

	@Override
	public boolean isActive() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA &&
				(isClient() ? Minecraft.getMinecraft().isSnooperEnabled() : MinecraftServer.getServer().isSnooperEnabled());
	}
}
