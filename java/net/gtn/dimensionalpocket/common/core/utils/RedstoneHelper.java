package net.gtn.dimensionalpocket.common.core.utils;

import me.jezza.oc.common.utils.CoordSet;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneHelper {

    /**
     * The current strength of the redstone that block is receiving from that side.
     */
    public static int getCurrentSignal(World world, CoordSet coordSet, ForgeDirection direction) {
        int neighbourX = coordSet.getX() + direction.offsetX;
        int neighbourY = coordSet.getY() + direction.offsetY;
        int neighbourZ = coordSet.getZ() + direction.offsetZ;

        Block neighbourBlock = world.getBlock(neighbourX, neighbourY, neighbourZ);

        int weak = neighbourBlock.isProvidingWeakPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
        int strong = neighbourBlock.isProvidingStrongPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
        return Math.max(weak, strong);
    }
}
