package net.gtn.dimensionalpocket.client.event;

import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
    
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
                        ChatComponentText text = new ChatComponentText(Utils.translate("functionality.disabled.in.pocket"));
                        text.getChatStyle().setItalic(Boolean.TRUE).setColor(EnumChatFormatting.GRAY);
                        player.addChatMessage(text);
                    }
                }
            }
        }
    }
}
