package net.gtn.dimensionalpocket.client.gui.components;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.client.gui.framework.GuiTexturedButton;
import net.minecraft.util.EnumChatFormatting;

public class GuiExitButton extends GuiTexturedButton {

    public GuiExitButton(int x, int y) {
        super(x, y, 77, 19, 11, 11);
    }

    @Override
    public boolean canClick(int mouseX, int mouseY) {
        if (isShiftKeyDown())
            return super.canClick(mouseX, mouseY);
        return false;
    }

    @Override
    public int getTextureXShift(int pass) {
        return 16 * pass;
    }

    @Override
    public void postRender(int mouseX, int mouseY) {
        if (isHoveringOver(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(isShiftKeyDown() ? (EnumChatFormatting.RED + "Reset to Default State") : ("Hold Down Shift"));
            renderHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
    }

}
