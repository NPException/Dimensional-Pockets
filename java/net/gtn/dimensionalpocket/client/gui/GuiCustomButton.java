package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.common.core.interfaces.IGuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiCustomButton extends Gui implements IGuiButton {

    public final TextureState textureState;
    public int x, y;

    public long timeClicked = 0;
    public boolean clicked = false;
    public boolean visible = true;

    public GuiCustomButton(int x, int y, int texX, int texY, int width, int height) {
        this(x, y, new TextureState(texX, texY, width, height));
    }

    public GuiCustomButton(int x, int y, TextureState textureState) {
        this.x = x;
        this.y = y;
        this.textureState = textureState;
    }

    public GuiCustomButton setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public GuiCustomButton setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public TextureState getTextureState() {
        return textureState;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean onClick(int mouseX, int mouseY) {
        timeClicked = System.currentTimeMillis();
        return clicked = canClick(mouseX, mouseY);
    }

    @Override
    public boolean canClick(int mouseX, int mouseY) {
        return visible && x < mouseX && mouseX < (x + textureState.getWidth()) && y < mouseY && mouseY < (y + textureState.getHeight());
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!visible)
            return;

        int tempX = textureState.getTexX();
        int tempY = textureState.getTexY();
        if (clicked || canClick(mouseX, mouseY)) {
            tempX += getTextureXShift();
            tempY += getTextureYShift();
        }

        if (clicked) {
            tempX += getTextureXShift();
            tempY += getTextureYShift();
            if (System.currentTimeMillis() - timeClicked > getButtonDelay())
                clicked = false;
        }
        drawTexturedModalRect(x, y, tempX, tempY, textureState.getWidth(), textureState.getHeight());
    }

    public void playButtonClick() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public abstract int getTextureXShift();

    public abstract int getTextureYShift();

    public abstract int getButtonDelay();
}
