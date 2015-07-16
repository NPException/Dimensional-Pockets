package net.gtn.dimensionalpocket.client.utils;

import net.gtn.dimensionalpocket.common.ModBlocks;
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
		new ItemStack(Items.ender_eye),
		new ItemStack(ModItems.endCrystal)
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
		new ItemStack(Items.ghast_tear),
		new ItemStack(ModItems.netherCrystal)
	};

	private static ItemStack[] blockArray = new ItemStack[] {
		new ItemStack(Blocks.stonebrick),
		new ItemStack(ModItems.endCrystal),
		new ItemStack(Blocks.stonebrick),
		new ItemStack(Blocks.iron_block),
		new ItemStack(Blocks.diamond_block),
		new ItemStack(Blocks.iron_block),
		new ItemStack(Blocks.stonebrick),
		new ItemStack(ModItems.netherCrystal),
		new ItemStack(Blocks.stonebrick),
		new ItemStack(ModBlocks.dimensionalPocket)
	};

	//@formatter:on

	public static ItemStack[] getRecipe(int type) {
		switch (type) {
			case 0:
				return blockArray;
			case 1:
				return enderArray;
			case 2:
				return netherArray;
			default:
				return null;
		}
	}
}
