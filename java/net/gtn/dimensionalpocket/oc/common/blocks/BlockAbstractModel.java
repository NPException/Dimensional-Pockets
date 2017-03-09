package net.gtn.dimensionalpocket.oc.common.blocks;

import net.minecraft.block.material.Material;

public abstract class BlockAbstractModel extends BlockAbstractGlass {

    public BlockAbstractModel(Material material, String name) {
        super(material, name);
        setTextureless();
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}
