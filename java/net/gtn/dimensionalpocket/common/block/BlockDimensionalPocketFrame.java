package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDPFrame;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name) {
        super(material, name);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightLevel(1);
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return true;

        ItemStack itemStack = player.getCurrentEquippedItem();

        if (itemStack != null) {
            if (itemStack.getItemDamage() == 0 || itemStack.getItemDamage() == 1) {
                if (player.dimension != Reference.DIMENSION_ID || world.isRemote)
                    return true;

                if (itemStack.getItem() == ModItems.craftingItems && (itemStack.getItemDamage() == 0 || itemStack.getItemDamage() == 1)) {
                    CoordSet coordSet = new CoordSet(x, y, z);

                    Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
                    if (pocket == null) {
                        return false;
                    }

                    boolean setSpawn = pocket.setSpawnSet(coordSet.asSpawnPoint());

                    if (setSpawn)
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    return true;
                }
            }
            return false;
        }

        if (!player.isSneaking())
            return true;

        if (!world.isRemote) {
            if (player.dimension != Reference.DIMENSION_ID)
                return true;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return true;

            pocket.teleportFrom(player);
        }

        return true;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileDPFrame();
    }
    
    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        // TODO Auto-generated method stub
        return super.createTileEntity(world, metadata);
    }
}
