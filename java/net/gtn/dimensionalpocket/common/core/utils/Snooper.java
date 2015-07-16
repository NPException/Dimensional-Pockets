package net.gtn.dimensionalpocket.common.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Snooper {

	private Snooper() {
	}

	private static class Player {
		private final UUID uuid;
		private boolean loggedIn = false;
		private long logoutTime = -1;
		private boolean canSnoop = false;

		Player(UUID uuid) {
			this.uuid = uuid;
		}
	}

	private static final Map<UUID, Player> players = new HashMap<>(16);

	private static Player getOrCreate(UUID playerID) {
		Player player = players.get(playerID);
		if (player == null) {
			player = new Player(playerID);
			players.put(playerID, player);
		}
		return player;
	}

	/**
	 * Registers a player login
	 *
	 * @param playerID
	 */
	public static void playerLoggedOn(UUID playerID) {
		Player player = getOrCreate(playerID);
		player.loggedIn = true;
	}

	/**
	 * Registers a player logoff
	 *
	 * @param playerID
	 */
	public static void playerLoggedOff(UUID playerID) {
		Player player = getOrCreate(playerID);
		player.loggedIn = false;
		player.logoutTime = System.currentTimeMillis();
	}

	/**
	 * Sets the snooper setting that this player has sent.
	 *
	 * @param playerID
	 * @param canSnoop
	 */
	public static void playerAdjustSnoop(UUID playerID, boolean canSnoop) {
		Player player = getOrCreate(playerID);
		player.canSnoop = canSnoop;
	}
}
