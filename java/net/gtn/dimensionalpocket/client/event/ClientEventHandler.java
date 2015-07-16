package net.gtn.dimensionalpocket.client.event;

import me.jezza.oc.common.utils.Localise;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	public static int gameTicks = 0;

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if (gui == null || !gui.doesGuiPauseGame()) {
				gameTicks++;
			}
		}
	}

	private long lastTravelStaffWarningTime = 0;

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		if (event.buttonstate && (event.button == 0 || event.button == 1)) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null && player.isSneaking() && player.worldObj.provider.dimensionId == Reference.DIMENSION_ID) {
				ItemStack stack = player.getCurrentEquippedItem();
				// stop EnderIO Travelers Staff from working in a DP
				if (stack != null && "item.itemTravelStaff".equals(stack.getUnlocalizedName())) {
					event.setCanceled(true);
					if (System.currentTimeMillis() > (lastTravelStaffWarningTime + 3000)) { // 3 second nag delay
						lastTravelStaffWarningTime = System.currentTimeMillis();
						ChatComponentText text = new ChatComponentText(Localise.translate("functionality.disabled.in.pocket"));
						text.getChatStyle().setItalic(Boolean.TRUE).setColor(EnumChatFormatting.GRAY);
						player.addChatMessage(text);
					}
				}
			}
		}
	}
}
