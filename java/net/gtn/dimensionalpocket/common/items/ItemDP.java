package net.gtn.dimensionalpocket.common.items;

import static net.gtn.dimensionalpocket.common.core.utils.DPAnalytics.*;
import me.jezza.oc.common.items.ItemAbstract;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemDP extends ItemAbstract {

	public ItemDP(String name) {
		super(name);
		setCreativeTab(DimensionalPockets.creativeTab);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote)
			return;

		if (analytics.isActive()) {
			analytics.logItemCrafted(stack.getUnlocalizedName(), stack.stackSize);
		}
	}
}
