package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

    // Break time
    static Block dimensionalPocket;

    public static void init() {
        dimensionalPocket = new BlockDimensionalPocket(Material.anvil, Strings.BLOCK_POCKET);
    }

}
