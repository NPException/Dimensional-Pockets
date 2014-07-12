package net.gtn.dimensionalpocket.common.core.interfaces;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

public interface IGuiRenderHandler {

    public GuiScreen getGuiScreen();

    public void renderTooltip(ItemStack itemStack, int x, int y);

    public void renderHoveringText(List list, int x, int y, FontRenderer font);

}
