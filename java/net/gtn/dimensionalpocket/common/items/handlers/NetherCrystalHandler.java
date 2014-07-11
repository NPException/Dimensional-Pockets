package net.gtn.dimensionalpocket.common.items.handlers;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NetherCrystalHandler extends UsableHandlerAbstract {

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = coordSet.getTileEntity(world);
        if (tileEntity instanceof TileDimensionalPocket) {
            player.openGui(DimensionalPockets.instance, 1, world, coordSet.getX(), coordSet.getY(), coordSet.getZ());
            return true;
        }
        return false;
    }

}
