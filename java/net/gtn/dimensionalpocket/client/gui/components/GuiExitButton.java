package net.gtn.dimensionalpocket.client.gui.components;

import net.gtn.dimensionalpocket.client.gui.framework.GuiTexturedButton;

public class GuiExitButton extends GuiTexturedButton {

    public GuiExitButton(int x, int y) {
        super(x, y, 77, 19, 11, 11);
    }

    @Override
    public int getTextureXShift(int pass) {
        return 16 * pass;
    }

}
