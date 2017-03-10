package net.gtn.dimensionalpocket.common.items;

import static net.gtn.dimensionalpocket.common.lib.Reference.CRAFTINGS_PER_CRYSTAL;

import java.util.List;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.lib.Hacks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.oc.common.interfaces.IItemTooltip;
import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.oc.common.utils.Localise;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;


public class ItemEndCrystal extends ItemDP {

	public ItemEndCrystal(String name) {
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
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (player.dimension != Reference.DIMENSION_ID) {
			return itemStack;
		}

		if (world.isRemote) {
			player.swingItem();
			return itemStack;
		}

		CoordSet coordSet = new CoordSet(player);
		Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
		if (pocket == null) {
			return itemStack;
		}

		pocket.setSpawnInPocket(Hacks.toChunkOffset(coordSet), player.rotationYaw, player.rotationPitch);

		ChatComponentTranslation comp = new ChatComponentTranslation("info.spawn.set.in.pocket");
		comp.getChatStyle().setItalic(Boolean.TRUE);
		player.addChatMessage(comp);

		return itemStack;
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
		String text = Localise.translate("info.tooltip.endCrystal.shift");
		List<String> lines = Localise.wrapToSize(text, 40);
		information.addAllToShiftList(lines);
	}
}
