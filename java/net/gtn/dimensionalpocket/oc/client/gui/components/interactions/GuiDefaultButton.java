package net.gtn.dimensionalpocket.oc.client.gui.components.interactions;

import net.gtn.dimensionalpocket.oc.client.gui.components.GuiTexturedButtonExpandable;
import net.gtn.dimensionalpocket.oc.client.gui.interfaces.ITextAlignment;
import net.gtn.dimensionalpocket.oc.client.lib.ResourceHelper;
import net.gtn.dimensionalpocket.oc.client.gui.lib.TextAlignment;
import net.gtn.dimensionalpocket.oc.client.lib.Colour;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class GuiDefaultButton extends GuiTexturedButtonExpandable<GuiDefaultButton> {
    public static final ResourceLocation BUTTON_TEXTURE = ResourceHelper.getOCTexture("gui/defaultButtons.png");

    public final String text;
    protected int textStartX, textStartY, colour;

    public GuiDefaultButton(int x, int y, int width, int height, String text) {
        super(x, y, 0, 0, width, height, 200, 20, 2, 3, 2, 2);
        setTexture(BUTTON_TEXTURE);
        this.text = text;
        setTextAlignment(TextAlignment.CENTRE);
        setColour(Colour.WHITE);
    }

    public GuiDefaultButton setTextAlignment(ITextAlignment textAlignment) {
        textStartX = textAlignment.translateX(this.width, text);
        textStartY = textAlignment.translateY(this.height, text);
        return this;
    }

    public GuiDefaultButton setColour(Colour colour) {
        this.colour = colour.getInt();
        return this;
    }

    public GuiDefaultButton setColour(int colour) {
        this.colour = colour;
        return this;
    }

    @Override
    public GuiDefaultButton setDisabled() {
        colour = Colour.GREY.getInt();
        return super.setDisabled();
    }

    @Override
    public GuiDefaultButton setEnabled() {
        colour = Colour.WHITE.getInt();
        return super.setEnabled();
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        super.renderBackground(mouseX, mouseY);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawText();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void drawText() {
        this.drawString(fontRendererObj, text, x + textStartX, y + textStartY, colour);
    }

    @Override
    public int getPassLevel(int mouseX, int mouseY) {
        return isEnabled() ? isWithinBounds(mouseX, mouseY) ? 2 : 1 : 0;
    }

    @Override
    public int getTextureYShift(int pass) {
        return pass * 20;
    }
}
