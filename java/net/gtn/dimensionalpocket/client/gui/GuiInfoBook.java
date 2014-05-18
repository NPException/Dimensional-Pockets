package net.gtn.dimensionalpocket.client.gui;

import static org.lwjgl.opengl.GL11.*;
import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.GuiSheet;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

public class GuiInfoBook extends GuiContainer {

    private GuiArrow arrow1;
    private GuiArrow arrow2;

    private int currentPage;
    private int MAX_NUM;

    public GuiInfoBook() {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return true;
            }
        });

        currentPage = ClientProxy.currentPage;

        xSize = 154;
        ySize = 180;

        String num = StatCollector.translateToLocal("info.page.maxPage");
        try {
            MAX_NUM = Integer.parseInt(num);
        } catch (NumberFormatException exception) {
            DPLogger.severe("Error in current .lang file. Please correct the info.page.maxPage to a proper number.");
            MAX_NUM = 5;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        initArrows();
    }

    private void initArrows() {
        int x = guiLeft + (xSize / 2) - (GuiArrow.WIDTH / 2) + 2;
        int y = guiTop + ySize - (GuiArrow.HEIGHT * 2);

        arrow1 = new GuiArrow(1, x + 44, y);
        arrow2 = new GuiArrow(2, x - 44, y);
    }

    @Override
    public void onGuiClosed() {
        ClientProxy.currentPage = currentPage;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        mc.renderEngine.bindTexture(GuiSheet.GUI_INFO_BOOK);

        drawTexturedModalRect(x, y, 12, 1, xSize, ySize);
        arrow1.renderArrow(mouseX, mouseY);
        arrow2.renderArrow(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        glPushMatrix();

        float scale = 0.75F;
        glScalef(scale, scale, scale);

        String tempString = StatCollector.translateToLocal("info.page." + currentPage);

        drawCentredString(tempString, 0, 0, 140, new Colour(0.2F, 0.2F, 0.2F, 1.0F));
        glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        if (arrow1.onClick(mouseX, mouseY))
            currentPage++;
        if (arrow2.onClick(mouseX, mouseY))
            currentPage--;

        if (currentPage < 0)
            currentPage = 0;
        if (currentPage > MAX_NUM)
            currentPage = MAX_NUM;
    }

    protected void drawCentredString(String string, int xOffset, int yOffset, int length, Colour colour) {
        int index = 0;
        for (String str : string.split("<br>")) {
            int x = (xSize - 135) / 2 + 30;
            int y = ySize / 2 - 70;

            fontRendererObj.drawSplitString(str, x + xOffset, y + xOffset + (fontRendererObj.FONT_HEIGHT * index++), length, colour.getInt());
        }
    }
}
