package net.gtn.dimensionalpocket.client.gui.framework;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.gtn.dimensionalpocket.common.core.interfaces.IClickHandler;
import net.gtn.dimensionalpocket.common.core.interfaces.IGuiButton;
import net.gtn.dimensionalpocket.common.core.interfaces.IGuiRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GuiWidget extends Gui implements IGuiButton {

    public FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    protected static RenderItem itemRender = new RenderItem();

    private IClickHandler clickHandler;
    private IGuiRenderHandler renderHandler;

    private int id = 0;
    public int x, y, width, height;

    public long timeClicked = 0;
    public boolean clicked = false;
    public boolean visible = true;

    public GuiWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GuiWidget setClickHandler(IClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public GuiWidget setRenderHandler(IGuiRenderHandler renderHandler) {
        this.renderHandler = renderHandler;
        return this;
    }

    public GuiWidget setID(int id) {
        this.id = id;
        return this;
    }

    public GuiWidget setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public GuiWidget setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public GuiWidget setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            clicked = false;
            timeClicked = 0;
        }
        return this;
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseClick) {
        clicked = canClick(mouseX, mouseY);
        if (clicked) {
            timeClicked = System.currentTimeMillis();
            if (clickHandler != null)
                clickHandler.onButtonClicked(this, mouseClick);
            if (shouldPlaySoundOnClick())
                playButtonClick();
        }
        return clicked;
    }

    @Override
    public boolean canClick(int mouseX, int mouseY) {
        return visible && isHoveringOver(mouseX, mouseY);
    }

    @Override
    public boolean isHoveringOver(int mouseX, int mouseY) {
        return x < mouseX && mouseX < (x + width) && y < mouseY && mouseY < (y + height);
    }

    public void playButtonClick() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public boolean shouldPlaySoundOnClick() {
        return true;
    }

    public void renderToolTip(ItemStack itemStack, int x, int y) {
        if (renderHandler != null)
            renderHandler.renderTooltip(itemStack, x, y);
    }

    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        if (renderHandler != null)
            renderHandler.renderHoveringText(list, x, y, font);
    }

    public void postRender(int mouseX, int mouseY) {
    };

    public boolean isAltKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    public boolean isControlKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
}
