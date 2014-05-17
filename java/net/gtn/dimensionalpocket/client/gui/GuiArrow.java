package net.gtn.dimensionalpocket.client.gui;

import net.minecraft.client.gui.Gui;

public class GuiArrow extends Gui {

    public int XPOS = 3;
    public int YPOS = 194;

    public static final int WIDTH = 18;
    public static final int HEIGHT = 10;

    public static final int INCREMENTAL_WIDTH = 23;
    public static final int INCREMENTAL_HEIGHT = 13;

    private int id, x, y;

    public GuiArrow(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public boolean canClick(int mouseX, int mouseY) {
        return x < mouseX && mouseX < (x + WIDTH) && y < mouseY && mouseY < (y + HEIGHT);
    }

    public void renderArrow(int mouseX, int mouseY, int mouseClick) {

        int tempX = XPOS;
        int tempY = YPOS;
        
        if (id == 2)
            tempY += INCREMENTAL_HEIGHT;

        drawTexturedModalRect(x, y, tempX, tempY, WIDTH, HEIGHT);
    }

    public static enum ArrowState {
        NATURAL, HOVERED, CLICKED;
    }

}
