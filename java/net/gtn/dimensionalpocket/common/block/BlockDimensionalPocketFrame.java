package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.handlers.RedstoneStateHandler;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name) {
        super(material, name);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightLevel(1F);
        setLightOpacity(255);
        useNeighborBrightness = false;
        disableStats();
        setCreativeTab(null);
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.provider.dimensionId == Reference.DIMENSION_ID;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
        if (pocket == null || !pocket.isSourceBlockPlaced())
            return 0;

        ForgeDirection pocketSide = Pocket.getSideForBlock(new CoordSet(x, y, z).asSpawnPoint());

        RedstoneStateHandler state = pocket.getRedstoneState();
        return state.getInput(pocketSide.ordinal());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        if (!player.isSneaking() || player.getCurrentEquippedItem() != null)
            return false;

        if (player.dimension == Reference.DIMENSION_ID) {
            if (world.isRemote)
                return false;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return false;

            if (pocket.getBlockDim() != Reference.DIMENSION_ID)
                player.setSneaking(false);

            pocket.teleportFrom(player);
            return true;
        }

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        CoordSet blockSet = new CoordSet(x, y, z);
        ForgeDirection direction = Pocket.getSideForBlock(blockSet.toSpawnPoint()).getOpposite();
        Pocket pocket = PocketRegistry.getPocket(blockSet.toChunkCoords());

        if (pocket == null || direction == ForgeDirection.UNKNOWN || block == Blocks.air ? false : world.isAirBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
            return;

        pocket.onNeighbourBlockChangedPocket(direction.getOpposite(), blockSet, block);
    }
}
