package net.gtn.dimensionalpocket.client.gui.components;

import me.jezza.oc.client.gui.components.GuiWidget;
import net.gtn.dimensionalpocket.client.utils.RenderUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class GuiItemStack extends GuiWidget<GuiItemStack> {

    private ItemStack itemStack = new ItemStack(Blocks.stone);

    public GuiItemStack(ItemStack itemStack, int x, int y) {
        super(x, y, 19, 19);
        this.itemStack = itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean shouldPlaySoundOnClick() {
        return false;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        RenderUtils.renderItemStackInGUI(itemStack, fontRendererObj, itemRender, x + 2, y + 2, 100.0F);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
        if (canClick(mouseX, mouseY)) {
            renderToolTip(itemStack, mouseX, mouseY);
        }
    }
}
