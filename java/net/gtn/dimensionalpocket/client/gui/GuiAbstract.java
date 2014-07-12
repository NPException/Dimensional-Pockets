package net.gtn.dimensionalpocket.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.gtn.dimensionalpocket.client.gui.framework.GuiWidget;
import net.gtn.dimensionalpocket.common.core.interfaces.IClickHandler;
import net.gtn.dimensionalpocket.common.core.interfaces.IGuiRenderHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GuiAbstract extends GuiContainer implements IGuiRenderHandler, IClickHandler {

    public int middleX, middleY;
    public ResourceLocation mainTexture;

    private ArrayList<GuiWidget> buttonList;
    private int id = 0;

    public GuiAbstract(Container container) {
        super(container);
        buttonList = new ArrayList<GuiWidget>();
    }

    public GuiAbstract setMainTexture(ResourceLocation mainTexture) {
        this.mainTexture = mainTexture;
        return this;
    }

    public ArrayList<GuiWidget> getButtonList() {
        return buttonList;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        id = 0;

        middleX = (width - xSize) / 2;
        middleY = (height - ySize) / 2;
    }

    public int getNextID() {
        return id++;
    }

    public int addButton(GuiWidget widget) {
        buttonList.add(widget.setID(getNextID()).setClickHandler(this).setRenderHandler(this));
        return id;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        if (mainTexture != null)
            mc.renderEngine.bindTexture(mainTexture);

        for (GuiWidget widget : buttonList)
            widget.render(mouseX, mouseY);
        for (GuiWidget widget : buttonList)
            widget.postRender(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int t) {
        for (GuiWidget widget : buttonList)
            if (widget.canClick(mouseX, mouseY))
                widget.onClick(mouseX, mouseY);
    }

    @Override
    public GuiScreen getGuiScreen() {
        return this;
    }

    @Override
    public void renderTooltip(ItemStack itemStack, int x, int y) {
        if (itemStack != null)
            renderToolTip(itemStack, x, y);
    }

    @Override
    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        drawHoveringText(list, x, y, font);
    }
}
