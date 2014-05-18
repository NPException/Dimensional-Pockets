package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.RedstoneHelper;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocket extends BlockDP {

    private Set<CoordSet> blocksCurrentlyUpdatedByTiles = new HashSet<CoordSet>();
    private Set<CoordSet> blocksCurrentlyUpdatedByBlocks = new HashSet<CoordSet>();

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
        setResistance(15F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        RedstoneHelper.checkNeighboursAndUpdateInputStrength(world, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return true;

        if (player.getCurrentEquippedItem() != null)
            return false;

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket) {
            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
            tile.prepareTeleportIntoPocket(player);
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return false;
        // return side != -1;
    }

    // @formatter:off
    /**
     * This methods basically asks: is the Dimensional Pocket at the specified coordinates
     * providing power to side on an adjacent block.
     * So for an adjacent block to the south of the DP, side will be NORTH.
     * Everything is upside down. Jezza, you should feel right home here.
     */
    // @formatter:on
    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket) {
            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
            return tile.getPocket().getOutputSignal(ForgeDirection.getOrientation(side).getOpposite().ordinal());
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return false;
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

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        CoordSet coords = new CoordSet(x, y, z);
        if (blocksCurrentlyUpdatedByTiles.contains(coords))
            return;

        blocksCurrentlyUpdatedByTiles.add(coords);
        RedstoneHelper.checkNeighboursAndUpdateInputStrength(world, x, y, z);
        blocksCurrentlyUpdatedByTiles.remove(coords);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        CoordSet coords = new CoordSet(x, y, z);
        if (blocksCurrentlyUpdatedByBlocks.contains(coords))
            return;

        blocksCurrentlyUpdatedByBlocks.add(coords);
        RedstoneHelper.checkNeighboursAndUpdateInputStrength(world, x, y, z);
        blocksCurrentlyUpdatedByBlocks.remove(coords);
    }
}
