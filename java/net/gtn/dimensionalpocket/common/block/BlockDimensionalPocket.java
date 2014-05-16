package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.PocketDimensionHelper;
import net.gtn.dimensionalpocket.common.core.teleport.Pocket;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

                tile.getPocket().teleportTo(player);
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (itemStack.hasTagCompound() && tileEntity instanceof TileDimensionalPocket) {

            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
            tile.setPocket(CoordSet.readFromNBT(itemStack.getTagCompound()));

            if (tile.hasPocket())
                TeleportingRegistry.changeTeleportLink(tile.getPocket().getChunkCoords(), entityLiving.dimension, tile.getCoordSet());
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
