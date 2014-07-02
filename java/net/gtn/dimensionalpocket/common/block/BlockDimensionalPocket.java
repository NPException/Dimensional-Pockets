package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import cofh.api.block.IDismantleable;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.pocket.states.redstone.RedstoneSideState;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocket extends BlockDP implements IDismantleable {

    private IIcon[] icons = new IIcon[Reference.SIDE_STATE_COUNT];

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
        setResistance(15F);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket)
            return ((TileDimensionalPocket) tileEntity).getPocket().getRedstoneState().getOutput(side);
        return 0;
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileDimensionalPocket();
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
        if (world.isRemote)
            return null;

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileDimensionalPocket) {
            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;

            ItemStack itemStack = tile.generateItemStack();

            boolean flag = world.func_147480_a(x, y, z, false);

            if (flag) {
                if (returnBlock) {
                    player.inventory.addItemStackToInventory(itemStack);
                } else {
                    EntityItem entityItem = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, itemStack);
                    entityItem.delayBeforeCanPickup = 0;

                    world.spawnEntityInWorld(entityItem);
                }
                MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
                tile.unloadPocket();
            }

            return itemStack;
        }
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }
}
