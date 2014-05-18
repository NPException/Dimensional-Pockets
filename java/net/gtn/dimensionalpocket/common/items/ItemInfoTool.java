package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.items.framework.ItemDP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemInfoTool extends ItemDP {

    public ItemInfoTool(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            onRightClick(world, player);
        return super.onItemRightClick(itemStack, world, player);
    }

    public void onRightClick(World world, EntityPlayer player) {
        player.openGui(DimensionalPockets.instance, 0, world, 0, 0, 0);
    }
}
