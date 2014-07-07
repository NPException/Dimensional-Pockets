package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.client.utils.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class GuiItemStack extends GuiScreen {

    private ItemStack itemStack = new ItemStack(Blocks.stone);

    private int x, y;

    private static final int WIDTH = 18;
    private static final int LENGTH = 18;

    public GuiItemStack(ItemStack itemStack, int x, int y) {
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
    }

    public ItemStack doRender(int mouseX, int mouseY) {
        RenderUtils.renderItemStackInGUI(itemStack, fontRendererObj, itemRender, x + 1, y + 1, 100.0F);

        if (isMouseover(mouseX, mouseY))
            return itemStack;
        return null;
    }

    private boolean isMouseover(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < (x + WIDTH) && mouseY >= y && mouseY < (y + LENGTH);
    }
}
