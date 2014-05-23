package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name) {
        super(material, name);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightOpacity(15);
        useNeighborBrightness = false;
        disableStats();
        // setCreativeTab(null);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        CoordSet coordSet = new CoordSet(x, y, z);
        Pocket pocket = PocketRegistry.getPocket(coordSet.asChunkCoords());

        if (pocket != null)
            return pocket.getExternalLight();
        return 0;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }

    @Override
    public boolean canPlaceBlockAt(World world, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
        return world.provider.dimensionId == Reference.DIMENSION_ID;
    }

    // @Override
    // public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
    // // ForgeDirection pocketSide = Pocket.getSideForBlock(new CoordSet(x, y, z).asSpawnPoint());
    //
    // // Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
    //
    // // if (pocket == null)
    // return 0;
    //
    // // return pocket.getInputSignal(pocketSide.ordinal());
    // }

    // @Override
    // public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
    // return isProvidingWeakPower(world, x, y, z, side);
    // }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        if (!player.isSneaking() || player.getCurrentEquippedItem() != null) {
            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).toChunkCoords());
            DPLogger.info(pocket.getExternalLight());
            return true;
        }

        if (player.dimension == Reference.DIMENSION_ID) {
            player.setSneaking(false);
            if (world.isRemote)
                return true;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return false;

            pocket.teleportFrom(player);
        }

        return true;
    }

    // @Override
    // public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
    // RedstoneHelper.checkWallNeighbourAndUpdateOutputStrength(world, x, y, z);
    // }

    // @Override
    // public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    // RedstoneHelper.checkWallNeighbourAndUpdateOutputStrength(world, x, y, z);
    // }

    // @Override
    // public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
    // return side != -1;
    // }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
