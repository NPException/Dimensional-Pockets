package net.gtn.dimensionalpocket.oc.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * No touchy.
 * Use GuiSimpleTexturedButton instead of this.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiTexturedButton<T extends GuiTexturedButton> extends GuiWidget<T> {

    public int u, v;
    protected int buttonDelay = 125;

    protected ResourceLocation texture;

    public GuiTexturedButton(int x, int y, int u, int v, int width, int height) {
        super(x, y, width, height);
        this.u = u;
        this.v = v;
    }

    public T setTexture(ResourceLocation texture) {
        this.texture = texture;
        return (T) this;
    }

    public T setButtonDelay(int buttonDelay) {
        this.buttonDelay = buttonDelay;
        return (T) this;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        if (!isVisible())
            return;

        int tempX = u;
        int tempY = v;
        int i = getPassLevel(mouseX, mouseY);

        if (i > 0) {
            tempX += getTextureXShift(i);
            tempY += getTextureYShift(i);
        }
        timedOutClick();

        bindTexture();
        drawTexturedModalRect(x, y, tempX, tempY, width, height);
    }

    public void bindTexture() {
        if (texture != null)
            bindTexture(texture);
    }

    public int getPassLevel(int mouseX, int mouseY) {
        return isClicked() ? 2 : (canClick(mouseX, mouseY) ? 1 : 0);
    }

    public void timedOutClick() {
        if ((System.currentTimeMillis() - timeClicked) > buttonDelay)
            clicked = false;
    }

    public int getTextureXShift(int pass) {
        return 0;
    }

    public int getTextureYShift(int pass) {
        return 0;
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
    }
}
