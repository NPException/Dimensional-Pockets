package net.gtn.dimensionalpocket.client.utils;

import net.gtn.dimensionalpocket.common.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RecipeHelper {

    //@formatter:off
    private static ItemStack[] enderArray = new ItemStack[] {
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye),
        new ItemStack(Blocks.glass),
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye),
        new ItemStack(Items.ender_eye)
    };

    private static ItemStack[] netherArray = new ItemStack[] {
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Blocks.redstone_block),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear),
        new ItemStack(Items.ghast_tear)
    };

    private static ItemStack[] blockArray = new ItemStack[] {
        new ItemStack(Blocks.stonebrick),
        new ItemStack(ModItems.craftingItems,1,1),
        new ItemStack(Blocks.stonebrick),
        new ItemStack(Blocks.iron_block),
        new ItemStack(Blocks.diamond_block),
        new ItemStack(Blocks.iron_block),
        new ItemStack(Blocks.stonebrick),
        new ItemStack(ModItems.craftingItems,1,0),
        new ItemStack(Blocks.stonebrick)
    };

    //@formatter:on

    public static ItemStack[] getEnderRecipe() {
        return enderArray;
    }

    public static ItemStack[] getNetherRecipe() {
        return netherArray;
    }

    public static ItemStack[] getBlockRecipe() {
        return blockArray;
    }

}
