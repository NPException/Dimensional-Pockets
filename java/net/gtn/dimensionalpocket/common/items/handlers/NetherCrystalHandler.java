package net.gtn.dimensionalpocket.common.items.handlers;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NetherCrystalHandler extends UsableHandlerAbstract {

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        if (!(coordSet.getTileEntity(world) instanceof TileDimensionalPocket))
            return false;

        player.openGui(DimensionalPockets.instance, 1, world, coordSet.getX(), coordSet.getY(), coordSet.getZ());
        // if (!player.capabilities.isCreativeMode)
        // itemStack.stackSize--;
        player.swingItem();
        return !world.isRemote;
    }
}
