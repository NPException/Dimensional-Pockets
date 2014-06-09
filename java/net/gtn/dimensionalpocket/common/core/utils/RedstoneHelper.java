package net.gtn.dimensionalpocket.common.core.utils;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneHelper {

    /**
     * The current strength of the redstone that block is receiving from that side.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @return
     */
    public static int getCurrentOutput(World world, CoordSet coordSet, ForgeDirection direction) {
        int neighbourX = coordSet.getX() + direction.offsetX;
        int neighbourY = coordSet.getY() + direction.offsetY;
        int neighbourZ = coordSet.getZ() + direction.offsetZ;

        Block neighbourBlock = world.getBlock(neighbourX, neighbourY, neighbourZ);

        int weak = neighbourBlock.isProvidingWeakPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
        int strong = neighbourBlock.isProvidingStrongPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
        return Math.max(weak, strong);
    }
}
