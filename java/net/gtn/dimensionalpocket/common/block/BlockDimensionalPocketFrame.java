package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.sidestates.ISideState;
import net.gtn.dimensionalpocket.common.core.sidestates.RedstoneState;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.RedstoneHelper;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.provider.dimensionId == Reference.DIMENSION_ID;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
        if (pocket == null || !pocket.isSourceBlockPlaced())
            return 0;

        CoordSet coordSet = pocket.getBlockCoords();
        return RedstoneHelper.getCurrentBlockOuputStrength(pocket.getBlockWorld(), coordSet.getX(), coordSet.getY(), coordSet.getZ(), ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        if (!player.isSneaking() || player.getCurrentEquippedItem() != null)
            return false;

        if (player.dimension == Reference.DIMENSION_ID) {
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

    // This might get called if a level is within a 3x3x3, so I return if the only possible block is still air.
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        CoordSet blockSet = new CoordSet(x, y, z);
        ForgeDirection direction = Pocket.getSideForBlock(blockSet.toSpawnPoint()).getOpposite();
        Pocket pocket = PocketRegistry.getPocket(blockSet.toChunkCoords());

        // This just stops things like levers updating 2030189230781597293704827340 blocks next to it.
        if (block != Blocks.air && world.isAirBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
            return;

        pocket.onNeighbourBlockChangedPocket(direction, new CoordSet(x, y, z));
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
