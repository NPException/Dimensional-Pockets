package net.gtn.dimensionalpocket.common.items;

import java.util.List;

import me.jezza.oc.common.interfaces.IItemTooltip;
import me.jezza.oc.common.utils.CoordSet;
import me.jezza.oc.common.utils.Localise;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.lib.Hacks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;


public class ItemEndCrystal extends ItemDP {

	public ItemEndCrystal(String name) {
		super(name);
		setEffect();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (player.dimension != Reference.DIMENSION_ID)
			return itemStack;

		if (world.isRemote) {
			player.swingItem();
			return itemStack;
		}

		CoordSet coordSet = new CoordSet(player);
		Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
		if (pocket == null)
			return itemStack;

		pocket.setSpawnInPocket(Hacks.toChunkOffset(coordSet), player.rotationYaw, player.rotationPitch);

		ChatComponentTranslation comp = new ChatComponentTranslation("info.spawn.set.in.pocket");
		comp.getChatStyle().setItalic(Boolean.TRUE);
		player.addChatMessage(comp);

		return itemStack;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip information) {
		information.defaultInfoList();
		String text = Localise.translate("info.tooltip.endCrystal.shift");
		List<String> lines = Localise.wrapToSize(text, 40);
		information.addAllToShiftList(lines);
	}
}
