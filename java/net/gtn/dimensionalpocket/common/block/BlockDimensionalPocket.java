package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import me.jezza.oc.common.blocks.BlockAbstract;
import me.jezza.oc.common.interfaces.ITileProvider;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cofh.api.block.IDismantleable;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockDimensionalPocket extends BlockAbstract implements IDismantleable, ITileProvider {

	public BlockDimensionalPocket(Material material, String name) {
		super(material, name);
		setHardness(4F);
		setLightOpacity(255);
		setLightLevel(1F);
		setResistance(Reference.DIMENSIONAL_POCKET_RESISTANCE);
		setCreativeTab(DimensionalPockets.creativeTab);
		// make sure OC registers a texture.
		textureReg = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected String getTextureName() {
		// we use the empty texture, because we don't want random particles to be drawn on falling/running
		return "empty";
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
		if (world.isRemote)
			return null;

		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileDimensionalPocket) {
			TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
			ItemStack itemStack = tile.generateItemStackOnRemoval();

			boolean flag = world.func_147480_a(x, y, z, false);

			if (flag) {
				if (returnBlock) {
					player.inventory.addItemStackToInventory(itemStack);
					MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
				} else {
					Utils.spawnItemStack(itemStack, world, x + 0.5F, y + 0.5F, z + 0.5F, 0);
				}
			}

			return Lists.newArrayList(itemStack);
		}
		return null;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDimensionalPocket();
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
		// do nothing, since ItemStack spawning happens in the TileEntity
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<>();

		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileDimensionalPocket) {
				items.add(((TileDimensionalPocket) te).generateItemStackOnRemoval());
			}
		}
		return items;
	}
}
