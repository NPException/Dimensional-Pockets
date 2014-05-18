package net.gtn.dimensionalpocket.common.core.utils;

import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneHelper {
    public static void checkNeighboursAndUpdateInputStrength(IBlockAccess world, int x, int y, int z) {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            int neighbourX = x+direction.offsetX;
            int neighbourY = y+direction.offsetY;
            int neighbourZ = z+direction.offsetZ;
            
            Block neighbourBlock = world.getBlock(neighbourX, neighbourY, neighbourZ);
            
            int weak = neighbourBlock.isProvidingWeakPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
            int strong = neighbourBlock.isProvidingStrongPower(world, neighbourX, neighbourY, neighbourZ, direction.ordinal());
            int strength = Math.max(weak, strong);

            TileEntity tileEntity = world.getTileEntity(x, y, z);

            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;

            tile.getPocket().setInputSignal(direction.ordinal(), strength);
            DPLogger.info("Changed inputsignal: " + direction.name() + " to " + strength);
        }
    }
}
