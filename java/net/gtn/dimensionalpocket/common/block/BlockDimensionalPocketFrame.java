package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
        setCreativeTab(null);
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.provider.dimensionId == Reference.DIMENSION_ID;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
        if (pocket == null || !pocket.isSourceBlockPlaced())
            return 0;

        ForgeDirection pocketSide = Pocket.getSideForBlock(new CoordSet(x, y, z).asSpawnPoint());
        return pocket.getRedstoneState().getInput(pocketSide.ordinal());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        if (!player.isSneaking() || player.getCurrentEquippedItem() != null)
            return false;

        if (player.dimension == Reference.DIMENSION_ID) {
            // TODO Player sneaking is still buggy.
            player.setSneaking(false);
            if (world.isRemote)
                return true;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return true;

            pocket.teleportFrom(player);
        }

        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        CoordSet blockSet = new CoordSet(x, y, z);
        ForgeDirection direction = Pocket.getSideForBlock(blockSet.toSpawnPoint()).getOpposite();
        Pocket pocket = PocketRegistry.getPocket(blockSet.toChunkCoords());

        // Levers update 349782804789 blocks, but then I realised it's all the redstone stuff.. :/
        // || (block == Blocks.lever && world.isAirBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
        if (pocket == null || direction == ForgeDirection.UNKNOWN)
            return;

        pocket.onNeighbourBlockChangedPocket(direction.getOpposite(), new CoordSet(x, y, z), block);
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
