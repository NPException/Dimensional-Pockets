package net.gtn.dimensionalpocket.common.core.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IBlockInteract {

    /**
     * Return true if no more processing should be done.
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ);

}
