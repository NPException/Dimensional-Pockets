package net.gtn.dimensionalpocket.oc.common.blocks;

import net.minecraft.block.material.Material;

public abstract class BlockAbstractGlass extends BlockAbstract {

    protected BlockAbstractGlass(Material material, String name) {
        super(material, name);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
}

