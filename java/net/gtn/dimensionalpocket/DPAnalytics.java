/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import de.npe.gameanalytics.SimpleAnalytics;
import de.npe.gameanalytics.events.GADesignEvent;
import de.npe.gameanalytics.events.GAEvent;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends SimpleAnalytics {

	public static final DPAnalytics analytics = new DPAnalytics();

	private static final String MAIN_POCKET = "Pocket:";

	private static final String SUB_PLAYER = "Player:";
	private static final String SUB_TRANSFER = "Transfer:";
	private static final String SUB_STATE = "State:";
	private static final String SUB_CRAFTED = "Crafted:";

	private static final String ANALYITCS_PLAYER_TELEPORT = MAIN_POCKET + SUB_PLAYER + "Teleport";
	private static final String ANALYITCS_PLAYER_TRAPPED = MAIN_POCKET + SUB_PLAYER + "Trapped";
	private static final String ANALYITCS_TRANSFER_ENERGY_RF = MAIN_POCKET + SUB_TRANSFER + "EnergyRF";
	private static final String ANALYITCS_TRANSFER_FLUIDS = MAIN_POCKET + SUB_TRANSFER + "Fluids";
	private static final String ANALYTICS_POCKET_PLACED = MAIN_POCKET + SUB_STATE + "Placed";
	private static final String ANALYTICS_POCKET_MINED = MAIN_POCKET + SUB_STATE + "Mined";
	private static final String ANALYTICS_POCKET_CRAFTED_PLAYER = MAIN_POCKET + SUB_STATE + SUB_CRAFTED + "Player";
	private static final String ANALYTICS_POCKET_CRAFTED_AUTOMATION = MAIN_POCKET + SUB_STATE + SUB_CRAFTED + "Automation";


	public DPAnalytics() {
		super(Reference.VERSION, Reference.GA_GAME_KEY, Reference.GA_SECRET_KEY);
	}

	@Override
	public boolean isActive() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA &&
				(isClient() ? Minecraft.getMinecraft().isSnooperEnabled() : MinecraftServer.getServer().isSnooperEnabled());
	}

	////////////////////////////////////////////
	// Logging of player teleport (-attempts) //
	////////////////////////////////////////////

	private GAEvent teleportIn;

	public void logPlayerTeleportInEvent() {
		if (teleportIn == null) {
			teleportIn = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT, "to", Float.valueOf(1f));
		}
		event(teleportIn, false);
	}

	private GAEvent teleportOut;

	public void logPlayerTeleportOutEvent() {
		if (teleportOut == null) {
			teleportOut = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT, "from", Float.valueOf(1f));
		}
		event(teleportOut, false);
	}

	private GAEvent trappedNotPlaced;

	public void logPlayerTrappedNotPlacedEvent() {
		if (trappedNotPlaced == null) {
			trappedNotPlaced = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED, "inside - not placed", Float.valueOf(1f));
		}
		event(trappedNotPlaced, false);
	}

	private GAEvent trappedBlocked;

	public void logPlayerTrappedBlockedEvent() {
		if (trappedBlocked == null) {
			trappedBlocked = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED, "inside - blocked", Float.valueOf(1f));
		}
		event(trappedBlocked, false);
	}

	private GAEvent trappedOutside;

	public void logPlayerTrappedOutsideEvent() {
		if (trappedOutside == null) {
			trappedOutside = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED, "outside", Float.valueOf(1f));
		}
		event(trappedOutside, false);
	}

	///////////////////////////////////
	// Logging of RF energy transfer //
	///////////////////////////////////

	public void logRFTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF, "in", Integer.valueOf(amount));
	}

	public void logRFTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF, "out", Integer.valueOf(amount));
	}

	//////////////////////////////////
	// Logging of fluid mb transfer //
	//////////////////////////////////

	public void logFluidTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS, "in", Integer.valueOf(amount));
	}

	public void logFluidTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS, "out", Integer.valueOf(amount));
	}

	////////////////////////////////////////////////
	// Logging of pocket placed + mined + crafted //
	////////////////////////////////////////////////

	private GAEvent pocketPlaced;

	public void logPocketPlaced() {
		if (pocketPlaced == null) {
			pocketPlaced = new GADesignEvent(this, ANALYTICS_POCKET_PLACED, null, Float.valueOf(1f));
		}
		event(pocketPlaced, false);
	}

	private GAEvent pocketMined;

	public void logPocketMined() {
		if (pocketMined == null) {
			pocketMined = new GADesignEvent(this, ANALYTICS_POCKET_MINED, null, Float.valueOf(1f));
		}
		event(pocketMined, false);
	}

	public void logPocketsCraftedByPlayer(int amount) {
		eventDesign(ANALYTICS_POCKET_CRAFTED_PLAYER, Integer.valueOf(amount));
	}

	public void logPocketsCraftedByAutomation(int amount) {
		eventDesign(ANALYTICS_POCKET_CRAFTED_AUTOMATION, Integer.valueOf(amount));
	}
}
