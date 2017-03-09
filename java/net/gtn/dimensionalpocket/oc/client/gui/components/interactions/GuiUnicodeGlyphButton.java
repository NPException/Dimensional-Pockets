package net.gtn.dimensionalpocket.oc.client.gui.components.interactions;

import net.gtn.dimensionalpocket.oc.client.gui.interfaces.ITextAlignment;
import net.minecraft.util.MathHelper;

import static org.lwjgl.opengl.GL11.*;

public class GuiUnicodeGlyphButton extends GuiDefaultButton {

    private final String glyph;
    private float scale = 3.5F;

    public GuiUnicodeGlyphButton(int x, int y, int width, int height, String text, String glyph) {
        super(x, y, width, height, text);
        this.glyph = glyph;
        setDisabled();
    }

    /**
     * Probably not going to work correctly, don't touch it.
     */
    public GuiUnicodeGlyphButton setGlyphScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public GuiDefaultButton setTextAlignment(ITextAlignment textAlignment) {
        String mainText = glyph + " " + text;
        textStartX = textAlignment.translateX(this.width, mainText);
        textStartY = textAlignment.translateY(this.height, mainText);
        return this;
    }

    @Override
    protected void drawText() {
        glPushMatrix();
        glScalef(scale, scale, 1.0F);

        int xPos = MathHelper.floor_double((x + textStartX) / scale) - MathHelper.floor_double(scale) + 2;
        int yPos = MathHelper.floor_double((y + textStartY) / scale) - MathHelper.floor_double(scale) + 1;

        drawString(fontRendererObj, glyph, xPos, yPos, colour);
        glPopMatrix();

        drawString(fontRendererObj, text, x + textStartX + 15, y + textStartY, colour);
    }
}
