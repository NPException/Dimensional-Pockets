package net.gtn.dimensionalpocket.oc.common.interfaces;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Implement on a TileEntity
 */
public interface IBlockNotifier {

    public void onBlockRemoval(World world, int x, int y, int z);

    public void onBlockAdded(EntityLivingBase entityLivingBase, World world, int x, int y, int z, ItemStack itemStack);
    
    public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block);
    
    public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ);

}
