package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocket extends BlockDP {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return true;

        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileDimensionalPocket) {
                TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
                if (!tile.hasPocket())
                    tile.generateNewPocket();

                Pocket pocket = tile.getPocket();
                pocket.teleportTo(player);
            }
        }
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_) {
        DPLogger.info("Comparator");
        return super.getComparatorInputOverride(world, p_149736_2_, p_149736_3_, p_149736_4_, p_149736_5_);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean canProvidePower() {
        // DPLogger.info("Can provide power?");
        return super.canProvidePower();
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
        DPLogger.info("Called Strong");
        return super.isProvidingStrongPower(world, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        DPLogger.info(side);
        DPLogger.info("Called Weak");
        return super.isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        if (world.isRemote)
            return;

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (itemStack.hasTagCompound() && tileEntity instanceof TileDimensionalPocket) {

            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
            tile.setPocket(CoordSet.readFromNBT(itemStack.getTagCompound()));

            if (tile.hasPocket()) {
                DPLogger.info(tile.getPocket().getBlockCoords());
                DPLogger.info(tile.getPocket().getBlockDim());
                DPLogger.info(tile.getPocket().getChunkCoords());
                PocketRegistry.updatePocket(tile.getPocket().getChunkCoords(), entityLiving.dimension, tile.getCoordSet());
            }
            DPLogger.info(tile.getPocket().getBlockCoords());
            DPLogger.info(tile.getPocket().getBlockDim());
            DPLogger.info(tile.getPocket().getChunkCoords());
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileDimensionalPocket();
    }
}
