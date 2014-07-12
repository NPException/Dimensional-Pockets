package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.client.gui.components.GuiExitButton;
import net.gtn.dimensionalpocket.client.gui.framework.GuiUntexturedButton;
import net.gtn.dimensionalpocket.client.gui.framework.GuiWidget;
import net.gtn.dimensionalpocket.client.utils.GuiSheet;
import net.gtn.dimensionalpocket.common.core.container.ConfigContainer;
import net.gtn.dimensionalpocket.common.core.interfaces.IClickHandler;
import net.gtn.dimensionalpocket.common.core.interfaces.IGuiRenderHandler;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiConfig extends GuiAbstract implements IClickHandler, IGuiRenderHandler {

    // private int side;

    public GuiConfig(TileDimensionalPocket tile, int side) {
        super(new ConfigContainer(tile));
        DPLogger.info(ForgeDirection.getOrientation(side));
        xSize = 74;
        ySize = 74;
    }

    @Override
    public void initGui() {
        super.initGui();
        initButtons();
    }

    private void initButtons() {
        int x = guiLeft;
        int y = guiTop;

        addButton(new GuiExitButton(x + (1 * 20) - 11, y + (1 * 20) - 11));
        addButton(x + (2 * 20) - 11, y + (1 * 20) - 11);
        for (int i = 1; i <= 3; i++)
            addButton(x + (i * 20) - 11, y + (2 * 20) - 11);
        for (int i = 2; i <= 3; i++)
            addButton(x + (i * 20) - 11, y + (3 * 20) - 11);
    }

    private void addButton(int x, int y) {
        addButton(new GuiUntexturedButton(x, y, 16, 16));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(GuiSheet.GUI_CONFIG);

        drawTexturedModalRect(middleX, middleY, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(var1, mouseX, mouseY);
    }

    @Override
    public void onButtonClicked(GuiWidget widget) {
        DPLogger.info(widget.getId());
    }
}
