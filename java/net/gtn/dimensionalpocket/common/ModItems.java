package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemCrafting;
import net.gtn.dimensionalpocket.common.items.ItemInfoTool;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    public static Item infoTool;
    public static Item craftingItems;

    public static void init() {
        infoTool = new ItemInfoTool(Strings.ITEM_INFO_TOOL);
        craftingItems = new ItemCrafting(Strings.ITEM_CRAFTING);
    }

    public static void initRecipes() {
        CraftingManager crafting = CraftingManager.getInstance();
        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket), new Object[] { "#N#", "IDI", "#E#", Character.valueOf('#'), new ItemStack(Blocks.stonebrick, 1, 3), Character.valueOf('I'), Blocks.iron_block, Character.valueOf('D'), Blocks.diamond_block, Character.valueOf('N'), new ItemStack(ModItems.craftingItems, 1, 2), Character.valueOf('E'), new ItemStack(ModItems.craftingItems, 1, 1) });
    }

}
