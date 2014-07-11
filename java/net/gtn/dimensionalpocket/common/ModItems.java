package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemUsable;
import net.gtn.dimensionalpocket.common.items.handlers.BookHandler;
import net.gtn.dimensionalpocket.common.items.handlers.EnderCrystalHandler;
import net.gtn.dimensionalpocket.common.items.handlers.NetherCrystalHandler;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    public static Item book;
    public static Item enderCrystal;
    public static Item netherCrystal;

    public static void init() {
        book = new ItemUsable(Strings.INFO_TOOL).setHandler(new BookHandler());
        enderCrystal = new ItemUsable(Strings.ENDER_CRYSTAL).setHandler(new EnderCrystalHandler());
        netherCrystal = new ItemUsable(Strings.NETHER_CRYSTAL).setHandler(new NetherCrystalHandler());
    }

    public static ItemStack getNetherCrystal() {
        return new ItemStack(netherCrystal);
    }

    public static ItemStack getEnderCrystal() {
        return new ItemStack(enderCrystal);
    }

    public static void initRecipes() {
        CraftingManager crafting = CraftingManager.getInstance();

        //@formatter:off
        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4), 
                new Object[] { "#N#",
                               "IDI",
                               "#E#",

                               Character.valueOf('#'),
                               new ItemStack(Blocks.stonebrick, 1, 0),

                               Character.valueOf('I'),
                               Blocks.iron_block,

                               Character.valueOf('D'),
                               Blocks.diamond_block,

                               Character.valueOf('N'),
                               netherCrystal,

                               Character.valueOf('E'),
                               enderCrystal
        });

        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4), 
                new Object[] { "#E#",
                               "IDI",
                               "#N#",
            
                               Character.valueOf('#'),
                               new ItemStack(Blocks.stonebrick, 1, 0),
            
                               Character.valueOf('I'),
                               Blocks.iron_block,
            
                               Character.valueOf('D'),
                               Blocks.diamond_block,
            
                               Character.valueOf('N'),
                               netherCrystal,
            
                               Character.valueOf('E'),
                               enderCrystal
        });
        
        crafting.addRecipe(getNetherCrystal(),
                new Object[] { "TTT",
                               "TRT",
                               "TTT",

                               Character.valueOf('T'),
                               Items.ghast_tear,

                               Character.valueOf('R'),
                               Blocks.redstone_block
        });
        
        crafting.addRecipe(getEnderCrystal(),
                new Object[] { "EEE",
                               "EGE",
                               "EEE",

                               Character.valueOf('E'),
                               Items.ender_eye,

                               Character.valueOf('G'),
                               Blocks.glass
        });
        
        crafting.addShapelessRecipe(new ItemStack(book),
                new Object[]{ Items.book, Items.leather
        });
        //@formatter:on
    }

}
