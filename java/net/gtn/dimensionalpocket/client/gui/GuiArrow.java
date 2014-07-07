package net.gtn.dimensionalpocket.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiArrow extends Gui {

    public int xPos = 3;
    public int yPos = 194;

    public static final int WIDTH = 18;
    public static final int HEIGHT = 10;

    public static final int INCREMENTAL_WIDTH = 23;
    public static final int INCREMENTAL_HEIGHT = 13;

    private ArrowType type;

    private int x, y;
    private long timeClicked;
    private boolean clicked = false;

    public GuiArrow(ArrowType type, int x, int y) {
        this.type = type;
        if (type == ArrowType.LEFT)
            yPos += 13;

        this.x = x;
        this.y = y;
    }

    public ArrowType getType() {
        return type;
    }

    public boolean onClick(int mouseX, int mouseY) {
        clicked = canClick(mouseX, mouseY);
        if (clicked) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            timeClicked = System.currentTimeMillis();
        }
        return clicked;
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

    public static enum ArrowType {
        LEFT, RIGHT;
    }
}
