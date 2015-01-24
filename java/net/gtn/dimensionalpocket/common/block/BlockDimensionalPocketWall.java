package net.gtn.dimensionalpocket.common.block;

import me.jezza.oc.common.blocks.BlockAbstract;
import me.jezza.oc.common.blocks.BlockAbstractModel;
import me.jezza.oc.common.interfaces.ITileProvider;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.items.handlers.NetherCrystalHandler;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocketWall extends BlockAbstractModel implements ITileProvider {
    
    public static final int CONNECTOR_META = 1;

    public BlockDimensionalPocketWall(Material material, String name) {
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
        
        ItemStack equippedItemStack = player.getCurrentEquippedItem();
        if (equippedItemStack != null) {
            if (Utils.isItemPocketWrench(equippedItemStack)) {
                new NetherCrystalHandler().onItemUseFirst(equippedItemStack, player, world, new CoordSet(x, y, z), side, hitVecX, hitVecY, hitVecZ);
                return true;
            }
            return false;
        }

        if (!player.isSneaking())
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
    
    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
       return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        if (metadata == CONNECTOR_META)
            return new TileDimensionalPocketWallConnector();
        return null;
    }

    @Override
    public String getModIdentifier() {
        return Reference.MOD_IDENTIFIER;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
