package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemCrafting;
import net.gtn.dimensionalpocket.common.items.ItemInfoTool;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    private static int ENDER_CRYSTAL_META = 0;
    private static int NETHER_CRYSTAL_META = 1;

    public static Item infoTool;
    public static Item craftingItems;

    public static void init() {
        infoTool = new ItemInfoTool(Strings.ITEM_INFO_TOOL);
        craftingItems = new ItemCrafting(Strings.ITEM_CRAFTING);
    }

    public static void initRecipes() {
        CraftingManager crafting = CraftingManager.getInstance();

        //@formatter:off
        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4), 
                new Object[] { "#N#",
                               "IDI",
                               "#E#",

                               Character.valueOf('#'),
                               new ItemStack(Blocks.stonebrick, 1, 2),

                               Character.valueOf('I'),
                               Blocks.iron_block,

                               Character.valueOf('D'),
                               Blocks.diamond_block,

                               Character.valueOf('N'),
                               new ItemStack(ModItems.craftingItems, 1, NETHER_CRYSTAL_META),

                               Character.valueOf('E'),
                               new ItemStack(ModItems.craftingItems, 1, ENDER_CRYSTAL_META)
        });
        
        crafting.addRecipe(new ItemStack(ModItems.craftingItems,1,NETHER_CRYSTAL_META),
                new Object[] { "TTT",
                               "TRT",
                               "TTT",

                               Character.valueOf('T'),
                               Items.ghast_tear,

                               Character.valueOf('R'),
                               Blocks.redstone_block
        });
        
        crafting.addRecipe(new ItemStack(ModItems.craftingItems,1,ENDER_CRYSTAL_META),
                new Object[] { "EEE",
                               "EGE",
                               "EEE",

                               Character.valueOf('E'),
                               Items.ender_eye,

                               Character.valueOf('G'),
                               Blocks.glass
        });
        
        crafting.addShapelessRecipe(new ItemStack(ModItems.infoTool),
                new Object[]{ Items.book, Items.leather
        });
        //@formatter:on
    }

}
