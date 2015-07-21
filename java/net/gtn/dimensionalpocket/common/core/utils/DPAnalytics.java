/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import de.npe.gameanalytics.SimpleAnalytics;
import de.npe.gameanalytics.events.GADesignEvent;
import de.npe.gameanalytics.events.GAErrorEvent.Severity;
import de.npe.gameanalytics.events.GAEvent;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends SimpleAnalytics {

	public static final DPAnalytics analytics = new DPAnalytics();

	private static final String MAIN_POCKET = "Pocket:";

	private static final String SUB_PLAYER = "Player:";
	private static final String SUB_TELEPORT = "Teleport:";
	private static final String SUB_TRANSFER = "Transfer:";
	private static final String SUB_STATE = "State:";
	private static final String SUB_CRAFTED = "Crafted:";
	private static final String SUB_ENERGY_RF = "EnergyRF:";
	private static final String SUB_FLUIDS = "Fluids:";
	private static final String SUB_TRAPPED = "Trapped:";

	private static final String TRAPPED_INSIDE_NOT_PLACED = "Inside_NotPlaced";
	private static final String TRAPPED_INSIDE_EXIT_BLOCKED = "Inside_ExitBlocked";
	private static final String TRAPPED_OUTSIDE_EXIT_BLOCKED = "Outside_ExitBlocked";
	private static final String DIRECTION_IN = "In";
	private static final String DIRECTION_OUT = "Out";


	private static final String ANALYITCS_PLAYER_TELEPORT_IN = MAIN_POCKET + SUB_PLAYER + SUB_TELEPORT + DIRECTION_IN;
	private static final String ANALYITCS_PLAYER_TELEPORT_OUT = MAIN_POCKET + SUB_PLAYER + SUB_TELEPORT + DIRECTION_OUT;
	private static final String ANALYITCS_PLAYER_TRAPPED_INSIDE_NOT_PLACED = MAIN_POCKET + SUB_PLAYER + SUB_TRAPPED + TRAPPED_INSIDE_NOT_PLACED;
	private static final String ANALYITCS_PLAYER_TRAPPED_INSIDE_EXIT_BLOCKED = MAIN_POCKET + SUB_PLAYER + SUB_TRAPPED + TRAPPED_INSIDE_EXIT_BLOCKED;
	private static final String ANALYITCS_PLAYER_TRAPPED_OUTSIDE_EXIT_BLOCKED = MAIN_POCKET + SUB_PLAYER + SUB_TRAPPED + TRAPPED_OUTSIDE_EXIT_BLOCKED;
	private static final String ANALYITCS_TRANSFER_ENERGY_RF_IN = MAIN_POCKET + SUB_TRANSFER + SUB_ENERGY_RF + DIRECTION_IN;
	private static final String ANALYITCS_TRANSFER_ENERGY_RF_OUT = MAIN_POCKET + SUB_TRANSFER + SUB_ENERGY_RF + DIRECTION_OUT;
	private static final String ANALYITCS_TRANSFER_FLUIDS_IN = MAIN_POCKET + SUB_TRANSFER + SUB_FLUIDS + DIRECTION_IN;
	private static final String ANALYITCS_TRANSFER_FLUIDS_OUT = MAIN_POCKET + SUB_TRANSFER + SUB_FLUIDS + DIRECTION_OUT;
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
			teleportIn = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT_IN, null, Float.valueOf(1f));
		}
		event(teleportIn, false);
	}

	private GAEvent teleportOut;

	public void logPlayerTeleportOutEvent() {
		if (teleportOut == null) {
			teleportOut = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT_OUT, null, Float.valueOf(1f));
		}
		event(teleportOut, false);
	}

	private GAEvent trappedNotPlaced;

	public void logPlayerTrappedInside_NotPlaced_Event() {
		if (trappedNotPlaced == null) {
			trappedNotPlaced = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_INSIDE_NOT_PLACED, null, Float.valueOf(1f));
		}
		event(trappedNotPlaced, false);
	}

	private GAEvent trappedBlocked;

	public void logPlayerTrappedInside_ExitBlocked_Event() {
		if (trappedBlocked == null) {
			trappedBlocked = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_INSIDE_EXIT_BLOCKED, null, Float.valueOf(1f));
		}
		event(trappedBlocked, false);
	}

	private GAEvent trappedOutside;

	public void logPlayerTrappedOutside_ExitBlocked_Event() {
		if (trappedOutside == null) {
			trappedOutside = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_OUTSIDE_EXIT_BLOCKED, null, Float.valueOf(1f));
		}
		event(trappedOutside, false);
	}

	///////////////////////////////////
	// Logging of RF energy transfer //
	///////////////////////////////////

	public void logRFTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF_IN, Integer.valueOf(amount));
	}

	public void logRFTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF_OUT, Integer.valueOf(amount));
	}

	//////////////////////////////////
	// Logging of fluid mb transfer //
	//////////////////////////////////

	public void logFluidTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS_IN, Integer.valueOf(amount));
	}

	public void logFluidTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS_OUT, Integer.valueOf(amount));
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

	///////////////////
	// Shutdown hook //
	///////////////////

	private static boolean hasRegisteredCrash;

	/**
	 * Creates a shutdown hook that looks for crashes
	 */
	public void initShutdownHook() {
		FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {
			@Override
			public String call() throws Exception {
				hasRegisteredCrash = true;
				return DPAnalytics.this.isActive() ? "Will analyze crash-log before shutdown and send error to developer if DimensionalPockets might be involved." : "[inactive]";
			}

			@Override
			public String getLabel() {
				return "DPAnalytics Crash Check";
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread("DPAnalytics-ShutdownHook") {
			@Override
			public void run() {
				if (DPAnalytics.this.isActive()) {
					if (hasRegisteredCrash) {
						System.out.println("Analyzing last crash log");
						DPAnalytics.this.eventErrorNOW(Severity.debug, "testing the send this error now thing with a thread");
					} else {
						System.out.println("No crash, we are good.");
					}
				}
			}
		});
	}
}
