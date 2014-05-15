package net.gtn.dimensionalpocket.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockDimensionalPocket extends BlockDP {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }

}
