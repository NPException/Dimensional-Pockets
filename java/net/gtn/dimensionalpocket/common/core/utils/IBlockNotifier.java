package net.gtn.dimensionalpocket.common.core.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockNotifier {

    public void onBlockPlaced(EntityLivingBase entityLiving, ItemStack itemStack);

    public void onBlockDestroyed();

    /**
     * Coords passed are its own, but the block is the block that an adjacent block changed to.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     */
    public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block);

}
