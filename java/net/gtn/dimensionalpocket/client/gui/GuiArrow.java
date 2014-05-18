package net.gtn.dimensionalpocket.client.gui;

import net.minecraft.client.gui.Gui;

public class GuiArrow extends Gui {

    public int xPos = 3;
    public int yPos = 194;

    public static final int WIDTH = 18;
    public static final int HEIGHT = 10;

    public static final int INCREMENTAL_WIDTH = 23;
    public static final int INCREMENTAL_HEIGHT = 13;

    private int x, y;
    private long timeClicked;
    private boolean clicked = false;

    public GuiArrow(int id, int x, int y) {
        if (id == 2)
            yPos += 13;

        this.x = x;
        this.y = y;
    }

    public boolean onClick(int mouseX, int mouseY) {
        boolean flag = canClick(mouseX, mouseY);
        clicked = flag;
        timeClicked = System.currentTimeMillis();
        return flag;
    }

    public boolean canClick(int mouseX, int mouseY) {
        return x < mouseX && mouseX < (x + WIDTH) && y < mouseY && mouseY < (y + HEIGHT);
    }

    public void renderArrow(int mouseX, int mouseY) {
        int tempX = xPos;
        int tempY = yPos;

        if (canClick(mouseX, mouseY) || clicked) {
            tempX += INCREMENTAL_WIDTH;
            if (clicked) {
                tempX += INCREMENTAL_WIDTH;
                if (System.currentTimeMillis() - timeClicked > 125)
                    clicked = false;
            }
        }

        drawTexturedModalRect(x, y, tempX, tempY, WIDTH, HEIGHT);
    }
}
