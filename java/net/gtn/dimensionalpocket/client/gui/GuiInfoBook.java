package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.common.lib.GuiSheet;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiInfoBook extends GuiContainer {

    public GuiInfoBook() {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return true;
            }
        });

        xSize = 154;
        ySize = 180;
    }

    @Override
    public void initGui() {
        super.initGui();

    }

    @Override
    public void drawDefaultBackground() {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK);

        drawTexturedModalRect(x, y, 12, 1, xSize, ySize);

        drawArrow(1);
        drawArrow(-1);
    }

    private void drawArrow(int direction) {
        int tempXSize = 18;
        int tempYSize = 10;

        int x = (width - tempXSize) / 2 + 2;
        int y = (height - tempYSize) / 2 - 2;

        int translateX = 44 * direction;
        x += translateX;

        int yPos = 194;
        if (direction == -1)
            yPos += 13;

        drawTexturedModalRect(x, y + (ySize / 2) - tempXSize, 3, yPos, tempXSize, tempYSize);
    }

}
