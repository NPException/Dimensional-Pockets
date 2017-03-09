package net.gtn.dimensionalpocket.oc.client.gui.components;

import net.gtn.dimensionalpocket.oc.client.gui.interfaces.IGuiRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class GuiWidget<T extends GuiWidget> extends Gui {

    public final FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    protected static RenderItem itemRender = new RenderItem();

    private IGuiRenderHandler renderHandler;

    private int id = 0;
    public int x, y, width, height;

    public long timeClicked = 0;
    public boolean clicked = false;
    private boolean visible = true;
    private boolean enabled = true;

    public GuiWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public T setRenderHandler(IGuiRenderHandler renderHandler) {
        this.renderHandler = renderHandler;
        return (T) this;
    }

    public T setID(int id) {
        this.id = id;
        return (T) this;
    }

    public T setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return (T) this;
    }

    public T setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    public T setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            clicked = false;
            timeClicked = 0;
        }
        return (T) this;
    }

    public T setEnabled() {
        this.enabled = true;
        return (T) this;
    }

    public T setDisabled() {
        this.enabled = false;
        return (T) this;
    }

    public int getId() {
        return id;
    }

    public void toggleVisibility() {
        visible = !visible;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean onClick(int mouseX, int mouseY, int mouseClick) {
        clicked = canClick(mouseX, mouseY);
        if (clicked) {
            timeClicked = System.currentTimeMillis();
            onClickAction(mouseClick);
            if (shouldPlaySoundOnClick())
                playButtonClick();
        }
        return clicked;
    }

    public void onClickAction(int mouseClick) {
    }

    public boolean canClick(int mouseX, int mouseY) {
        return visible && enabled && isWithinBounds(mouseX, mouseY);
    }

    public boolean isWithinBounds(int mouseX, int mouseY) {
        return x < mouseX && mouseX < (x + width) && y < mouseY && mouseY < (y + height);
    }

    public boolean shouldPlaySoundOnClick() {
        return true;
    }

    public void playButtonClick() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public void renderToolTip(ItemStack itemStack, int x, int y) {
        if (renderHandler != null)
            renderHandler.renderTooltip(itemStack, x, y);
    }

    public void renderHoveringText(List list, int x, int y, FontRenderer font) {
        if (renderHandler != null)
            renderHandler.renderHoveringText(list, x, y, font);
    }

    public abstract void renderBackground(int mouseX, int mouseY);

    public abstract void renderForeground(int mouseX, int mouseY);

    public boolean isAltKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    public boolean isControlKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public void bindTexture(ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
}
