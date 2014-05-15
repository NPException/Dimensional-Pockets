package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.block.BlockDP;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class ModBlocks {

    public static Block dimensionalPocket;
    public static Block dimensionalPocketFrame;

    public static void init() {
        dimensionalPocket = new BlockDimensionalPocket(Material.iron, Strings.BLOCK_POCKET);
        dimensionalPocketFrame = new BlockDimensionalPocketFrame(Material.anvil, Strings.BLOCK_POCKET_FRAME);
    }
}
