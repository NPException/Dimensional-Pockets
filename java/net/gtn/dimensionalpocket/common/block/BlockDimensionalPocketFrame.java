package net.gtn.dimensionalpocket.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name) {
        super(material, name);
        setBlockUnbreakable();
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return null;
    }

}
