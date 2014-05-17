package net.gtn.dimensionalpocket.client.gui;

import net.minecraft.client.gui.Gui;

public class GuiArrow extends Gui {

    public static final int WIDTH = 18;
    public static final int HEIGHT = 10;

    public static final int INCREMENTAL_WIDTH = 23;
    public static final int INCREMENTAL_HEIGHT = 13;

    private int x, y;

    public GuiArrow(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean canClick(int mouseX, int mouseY) {
        return x < mouseX && mouseX < (x + WIDTH) && y < mouseY && mouseY < (y + HEIGHT);
    }

    public static enum ArrowState {
        NATURAL, HOVERED, CLICKED;
    }

}
