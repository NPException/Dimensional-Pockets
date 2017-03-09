package net.gtn.dimensionalpocket.oc.common.interfaces;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileProvider extends ITileEntityProvider {

    TileEntity createNewTileEntity(World world, int meta);



}
