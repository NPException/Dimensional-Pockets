package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.GuiSheet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiInfoBook extends GuiContainer {

    ItemStack itemStack;

    GuiArrow arrow1;
    GuiArrow arrow2;

    public GuiInfoBook(ItemStack itemStack) {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return true;
            }
        });

        this.itemStack = itemStack;

        xSize = 154;
        ySize = 180;
        
        initArrows();
    }
    
    private void initArrows() {
        int x = (width - tempXSize) / 2 + 2;
        int y = (height - tempYSize) / 2 - 2 + (ySize / 2) - tempXSize;

        int translateX = 44 * direction;
        x += translateX;

        int yPos = 194;
        if (direction == -1)
            yPos += 13;
        
        
    }

    @Override
    public void drawDefaultBackground() {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseZ) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        DPLogger.info(mouseX);
        DPLogger.info(mouseZ);

        mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK);

        drawTexturedModalRect(x, y, 12, 1, xSize, ySize);
    }

    @Override
    public void updateScreen() {

    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {

    }
}
