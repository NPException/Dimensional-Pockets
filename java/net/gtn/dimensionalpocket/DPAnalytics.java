/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import de.npe.gameanalytics.Analytics;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends Analytics {
	public boolean isClient = false;

	private static final String gameKey = "da1d2ad5b654b795d187f0a2cc8d0609";
	private static final String secretKey = "760658a7191e5d29f3a6cc9f8ad325ed88631535";

	@Override
	public boolean active() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA && Minecraft.getMinecraft().isSnooperEnabled();
	}

	@Override
	public String gameKey() {
		return gameKey;
	}

	@Override
	public String secretKey() {
		return secretKey;
	}

	@Override
	public String build() {
		return Reference.VERSION;
	}

	@Override
	public String userPrefix() {
		return isClient ? "user" : "server";
	}
}
