package net.gtn.dimensionalpocket.common.block;

import cofh.api.block.IDismantleable;
import com.google.common.collect.Lists;
import me.jezza.oc.common.blocks.BlockAbstract;
import me.jezza.oc.common.interfaces.ITileProvider;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockDimensionalPocket extends BlockAbstract implements IDismantleable, ITileProvider {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
        setResistance(15F);
        setCreativeTab(DimensionalPockets.creativeTab);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
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
                    MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
                } else
                    Utils.spawnItemStack(itemStack, world, x + 0.5F, y + 0.5F, z + 0.5F, 0);
                tile.unloadPocket();
            }

            return Lists.newArrayList(itemStack);
        }
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }

    @Override
    public String getModIdentifier() {
        return Reference.MOD_IDENTIFIER;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileDimensionalPocket();
    }
}
