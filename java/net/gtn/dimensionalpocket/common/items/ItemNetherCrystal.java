package net.gtn.dimensionalpocket.common.items;

import static net.gtn.dimensionalpocket.common.lib.Reference.CRAFTINGS_PER_CRYSTAL;

import java.util.List;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.gtn.dimensionalpocket.oc.common.interfaces.IItemTooltip;
import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.oc.common.utils.Localise;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class ItemNetherCrystal extends ItemDP {

	public ItemNetherCrystal(String name) {
		super(name);
		setEffect();
		setMaxDurability(CRAFTINGS_PER_CRYSTAL);
		setNoRepair();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !(CRAFTINGS_PER_CRYSTAL > 0 && stack.getCurrentDurability() >= CRAFTINGS_PER_CRYSTAL);
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		if (CRAFTINGS_PER_CRYSTAL == 0) {
			return new ItemStack(this);
		}

		int damage = itemStack.getCurrentDurability() + 1;

		if (damage >= CRAFTINGS_PER_CRYSTAL) {
			return null;
		}

		return new ItemStack(this, itemStack.stackSize, damage);
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		CoordSet coordSet = new CoordSet(x, y, z);
		Block block = coordSet.getBlock(world);
		if (block != ModBlocks.dimensionalPocket && block != ModBlocks.dimensionalPocketWall) {
			return false;
		}

		if (world.isRemote) {
			player.swingItem();
			return false;
		}

		if (block == ModBlocks.dimensionalPocketWall) {
			if (world.provider.dimensionId != Reference.DIMENSION_ID) {
				return false;
			}

			Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
			if (pocket == null) {
				DPLogger.warning("Could not find pocket for ChunkSet: " + coordSet.toChunkCoords().toString());
				return false;
			}

			ForgeDirection wallSide = Pocket.getSideForConnector(coordSet.toChunkOffset());
			if (wallSide == ForgeDirection.UNKNOWN) {
				DPLogger.warning("Got ForgeDirection UNKNOWN for new Connector CoordSet: " + coordSet.toChunkOffset());
				return false;
			}

			if (player.isSneaking()) {
				CoordSet oldConnectorCoords = pocket.getConnectorCoords(wallSide);
				if (coordSet.equals(oldConnectorCoords)) {
					return false;
				}

				pocket.setConnectorForSide(wallSide, coordSet);
			} else {
				cyclePocketSideState(pocket, wallSide, player);
			}
		} else {
			TileEntity te = coordSet.getTileEntity(world);
			if (!(te instanceof TileDimensionalPocket)) {
				return false;
			}

			TileDimensionalPocket tdp = (TileDimensionalPocket) te;
			Pocket pocket = tdp.getPocket();

			ForgeDirection wallSide = ForgeDirection.getOrientation(side);
			cyclePocketSideState(pocket, wallSide, player);
		}

		return true;
	}

	private static void cyclePocketSideState(Pocket pocket, ForgeDirection wallSide, EntityPlayer player) {
		PocketSideState state = pocket.getFlowState(wallSide);
		int nextStateOrdinal = state.ordinal() + 1;
		if (nextStateOrdinal >= PocketSideState.values().length) {
			nextStateOrdinal = 0;
		}

		PocketSideState newState = PocketSideState.values()[nextStateOrdinal];
		pocket.setFlowState(wallSide, newState);

		ChatComponentText comp = new ChatComponentText(Localise.format("info.pocket.side.state.set.to", wallSide.name(), newState.translateName()));
		player.addChatMessage(comp);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip information) {
		final int damage = stack.getCurrentDurability();
		if (damage > 0) {
			int remaining = CRAFTINGS_PER_CRYSTAL - damage;
			final String text;
			if (remaining == 1) {
				text = Localise.format("info.tooltip.crystal.craft.remaining.singular");
			} else {
				text = Localise.format("info.tooltip.crystal.craft.remaining.plural", Integer.valueOf(remaining));
			}
			information.addAllToInfoList(Localise.wrapToSize(text, 40));
		}
		information.defaultInfoList();
		String text = Localise.translate("info.tooltip.netherCrystal.shift");
		List<String> lines = Localise.wrapToSize(text, 40);
		information.addAllToShiftList(lines);
	}
}
