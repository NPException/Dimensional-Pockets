/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import de.npe.gameanalytics.SimpleAnalytics;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends SimpleAnalytics {
	public DPAnalytics() {
		super(Reference.VERSION, Reference.GA_GAME_KEY, Reference.GA_SECRET_KEY);
	}

	@Override
	public boolean isActive() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA && Minecraft.getMinecraft().isSnooperEnabled();
	}
}
