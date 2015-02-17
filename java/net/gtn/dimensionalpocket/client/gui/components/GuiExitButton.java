package net.gtn.dimensionalpocket.client.gui.components;

import me.jezza.oc.client.gui.components.GuiTexturedButton;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

public class GuiExitButton extends GuiTexturedButton<GuiExitButton> {

    public GuiExitButton(int x, int y) {
        super(x, y, 77, 19, 11, 11);
    }

    @Override
    public boolean canClick(int mouseX, int mouseY) {
        return isShiftKeyDown() && super.canClick(mouseX, mouseY);
    }

    @Override
    public int getTextureXShift(int pass) {
        return 16 * pass;
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
        if (isWithinBounds(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(isShiftKeyDown() ? (EnumChatFormatting.RED + "Reset to Default State") : ("Hold Down Shift"));
            renderHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
    }

}
