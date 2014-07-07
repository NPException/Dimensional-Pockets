package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cofh.api.block.IDismantleable;

public class BlockDimensionalPocket extends BlockDP implements IDismantleable {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
        setResistance(15F);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        // TODO NP here is the issue.
        // This isn't getting called.
        // Deal with it.
        // Much love.
        // Suck it.
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket)
            return ((TileDimensionalPocket) tileEntity).getPocket().getRedstoneState().getOutput(side);
        return 0;
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
                    MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
                } else
                    Utils.spawnItemStack(itemStack, world, x + 0.5F, y + 0.5F, z + 0.5F, 0);
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
