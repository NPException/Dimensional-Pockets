package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

//@BlockRegistar(package = "block")
public class ModBlocks {

    public static Block dimensionalPocket;
    public static Block dimensionalPocketWall;
    
    public static void init() {
        dimensionalPocket = new BlockDimensionalPocket(Material.anvil, Strings.BLOCK_POCKET);
        dimensionalPocketWall = new BlockDimensionalPocketWall(Material.anvil, Strings.BLOCK_POCKET_WALL);
    }
}
