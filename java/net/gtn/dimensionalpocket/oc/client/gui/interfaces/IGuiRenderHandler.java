package net.gtn.dimensionalpocket.oc.client.gui.interfaces;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IGuiRenderHandler {

    public GuiScreen getGuiScreen();

    public void renderTooltip(ItemStack itemStack, int x, int y);

    public void renderHoveringText(List list, int x, int y, FontRenderer font);

}
