package net.gtn.dimensionalpocket.common.items.handlers;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BookHandler extends UsableHandlerAbstract {

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            player.openGui(DimensionalPockets.instance, 0, world, 0, 0, 0);
        return itemStack;
    }

}
