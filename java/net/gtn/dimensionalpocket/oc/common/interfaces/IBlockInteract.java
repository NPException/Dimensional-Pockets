package net.gtn.dimensionalpocket.oc.common.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Implement on a TileEntity
 */
public interface IBlockInteract {

    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ);

}