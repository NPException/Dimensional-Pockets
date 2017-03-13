package net.gtn.dimensionalpocket.client.event;

import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.gtn.dimensionalpocket.client.utils.version.VersionChecker;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.oc.common.utils.Localise;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;


@SideOnly(Side.CLIENT)
public class ClientPlayerTickEventHandler {

	public static boolean hideStuffFromNEI = false;

	private static boolean checkForVersion = Reference.DO_VERSION_CHECK;
	private static boolean suggestWiki = true;

	/**
	 * Hides blocks and items from NEI that it should not show. The Dimensional
	 * Pocket Wall f.e.
	 */
	private static void hideStuffFromNEI() {
		try {
			Class<?> neiApiClass = Class.forName("codechicken.nei.api.API");
			Method hideItemMethod = neiApiClass.getDeclaredMethod("hideItem", ItemStack.class);
			hideItemMethod.invoke(null, new ItemStack(ModBlocks.dimensionalPocketWall));
		} catch (Exception e) {
			DPLogger.warning("could not finish method \"hideStuffFromNEI()\"");
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void event(PlayerTickEvent evt) {
		if (evt.player != Minecraft.getMinecraft().thePlayer) {
			return;
		}

		if (hideStuffFromNEI) {
			hideStuffFromNEI = false;
			hideStuffFromNEI();
		}

		if (checkForVersion) {
			checkForVersion = false;
			VersionChecker.checkUpToDate(evt.player);
		}

		ItemStack equippedItemStack = evt.player.getCurrentEquippedItem();
		Item equippedItem = equippedItemStack != null ? equippedItemStack.getItem() : null;

		// check for Nether Crystal in hand to trigger side color coding of pockets
		TileRendererPocket.doIndicateSides = equippedItem == ModItems.netherCrystal;

		// check for Guide Book and show wiki link
		if (suggestWiki && equippedItem == ModItems.book) {
			suggestWiki = false;
			evt.player.addChatMessage(new ChatComponentText(Localise.translate("info.wiki.1")));
			IChatComponent linkLine = new ChatComponentText(Localise.translate("info.wiki.2") + " -> [ ");
			linkLine.appendSibling(Utils.createChatLink(Localise.translate("click.me"), Reference.MOD_WIKI_URL, true, false, false, EnumChatFormatting.AQUA));
			linkLine.appendSibling(new ChatComponentText(" ]"));
			evt.player.addChatMessage(linkLine);
		}
	}
}
