package net.gtn.dimensionalpocket.common.block.framework;

import me.jezza.oc.common.blocks.BlockAbstract;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class BlockDP extends BlockAbstract {

    public BlockDP(Material material, String name) {
        super(material, name);
        setCreativeTab(DimensionalPockets.creativeTab);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public String getModIdentifier() {
        return Reference.MOD_IDENTIFIER;
    }
}
