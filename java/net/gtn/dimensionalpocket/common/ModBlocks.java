package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

    public static Block dimensionalPocket;
    public static Block[] dimensionalPocketFrames;

    public static void init() {
        dimensionalPocket = new BlockDimensionalPocket(Material.anvil, Strings.BLOCK_POCKET);
        
        dimensionalPocketFrames = new Block[16];
        for (int i = 0; i< dimensionalPocketFrames.length; i++) {
            float lightLevel = i / 15.0f;
            dimensionalPocketFrames[i] = new BlockDimensionalPocketFrame(Material.anvil, Strings.BLOCK_POCKET_FRAME + "_" + i,lightLevel);
        }
    }
}
