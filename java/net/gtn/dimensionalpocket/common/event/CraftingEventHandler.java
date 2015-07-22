/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.event;

import static net.gtn.dimensionalpocket.common.core.utils.DPAnalytics.*;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.minecraft.item.Item;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;


/**
 * @author NPException
 *
 */
public class CraftingEventHandler {

	private Item dpBlockItem;

	@SubscribeEvent
	public void onPlayerItemCraft(ItemCraftedEvent event) {
		if (dpBlockItem == null) {
			dpBlockItem = Item.getItemFromBlock(ModBlocks.dimensionalPocket);
		}
		if (event.crafting.getItem() == dpBlockItem && analytics.isActive()) {
			analytics.logItemCrafted(event.crafting.getUnlocalizedName(), event.crafting.stackSize);
		}
	}
}
