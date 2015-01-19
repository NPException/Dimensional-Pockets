package net.gtn.dimensionalpocket.common.block;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
    	// do nothing
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.provider.dimensionId == Reference.DIMENSION_ID;
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
        // do nothing
    }
}
