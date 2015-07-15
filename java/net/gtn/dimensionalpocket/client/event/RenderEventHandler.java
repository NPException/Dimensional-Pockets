package net.gtn.dimensionalpocket.client.event;

import net.minecraftforge.client.event.GuiScreenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEventHandler {
    
    public static boolean isRenderingGUI = false;
    
    @SubscribeEvent
    public void preGUIRenderTick(GuiScreenEvent.DrawScreenEvent.Pre event) {
        isRenderingGUI = true;
    }

    @SubscribeEvent
    public void postGUIRenderTick(GuiScreenEvent.DrawScreenEvent.Post event) {
        isRenderingGUI = false;
    }
}
