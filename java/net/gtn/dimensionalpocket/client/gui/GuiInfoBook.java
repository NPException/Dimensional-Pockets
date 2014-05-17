package net.gtn.dimensionalpocket.client.gui;

import static org.lwjgl.opengl.GL11.*;

import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.common.lib.GuiSheet;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GuiInfoBook extends GuiContainer {

    ItemStack itemStack;

    GuiArrow arrow1;
    GuiArrow arrow2;

    int currentPage;

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
    public void drawDefaultBackground() {
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
        String[] pageContents = getPageContents();

        glPushMatrix();

        float scale = 0.8F;
        glScalef(scale, scale, scale);

        drawString(fontRendererObj, pageContents[0], guiLeft - 50, guiTop, Colour.getInt(0.2F, 0.2F, 0.2F, 1.0F));
        glPopMatrix();
    }

    public String[] getPageContents() {
        String tempString = StatCollector.translateToLocal("info.page." + currentPage);

        return tempString.split("<br>");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        if (arrow1.onClick(mouseX, mouseY))
            currentPage++;
        if (arrow2.onClick(mouseX, mouseY))
            currentPage--;
    }
}
