package net.gtn.dimensionalpocket.common.items.handlers;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.interfaces.IUsable;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BookHandler implements IUsable {

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            player.openGui(DimensionalPockets.instance, 0, world, 0, 0, 0);
        return itemStack;
    }

}
