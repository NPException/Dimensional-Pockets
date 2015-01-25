package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemUsable;
import net.gtn.dimensionalpocket.common.items.handlers.BookHandler;
import net.gtn.dimensionalpocket.common.items.handlers.EnderCrystalHandler;
import net.gtn.dimensionalpocket.common.items.handlers.NetherCrystalHandler;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    public static ItemUsable book;
    public static ItemUsable enderCrystal;
    public static ItemUsable netherCrystal;

    public static void init() {
        book = new ItemUsable(Strings.INFO_BOOK, new BookHandler());
        enderCrystal = new ItemUsable(Strings.ENDER_CRYSTAL, new EnderCrystalHandler());
        enderCrystal.hasEffect = true;
        netherCrystal = new ItemUsable(Strings.NETHER_CRYSTAL, new NetherCrystalHandler());
        netherCrystal.hasEffect = true;
    }

    public static ItemStack getNetherCrystal() {
        return new ItemStack(netherCrystal);
    }

    public static ItemStack getEnderCrystal() {
        return new ItemStack(enderCrystal);
    }

    @SuppressWarnings("boxing")
    public static void initRecipes() {
        CraftingManager crafting = CraftingManager.getInstance();

        //@formatter:off
        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4), 
                "#N#",
                "IDI",
                "#E#",

                '#',
                new ItemStack(Blocks.stonebrick, 1, 0),

                'I',
                Blocks.iron_block,

                'D',
                Blocks.diamond_block,

                'N',
                netherCrystal,

                'E',
                enderCrystal
        );

        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4), 
                "#E#",
                "IDI",
                "#N#",
            
                '#',
                new ItemStack(Blocks.stonebrick, 1, 0),
            
                'I',
                Blocks.iron_block,
            
                'D',
                Blocks.diamond_block,
            
                'N',
                netherCrystal,
            
                'E',
                enderCrystal
        );
        
        crafting.addRecipe(getNetherCrystal(),
                "TTT",
                "TRT",
                "TTT",

                'T',
                Items.ghast_tear,

                'R',
                Blocks.redstone_block
        );
        
        crafting.addRecipe(getEnderCrystal(),
                "EEE",
                "EGE",
                "EEE",

                'E',
                Items.ender_eye,

                'G',
                Blocks.glass);
        
        crafting.addShapelessRecipe(new ItemStack(book),Items.book, Items.leather);
        //@formatter:on
    }

}
