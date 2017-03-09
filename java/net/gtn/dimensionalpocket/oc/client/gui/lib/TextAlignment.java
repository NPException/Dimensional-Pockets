package net.gtn.dimensionalpocket.oc.client.gui.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.client.gui.interfaces.ITextAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public enum TextAlignment implements ITextAlignment {
    TOP_LEFT(0, 0),
    TOP_CENTRE(1, 0),
    TOP_RIGHT(2, 0),
    LEFT(0, 1),
    CENTRE(1, 1),
    RIGHT(2, 1),
    BOTTOM_LEFT(0, 2),
    BOTTOM_CENTRE(1, 2),
    BOTTOM_RIGHT(2, 2);

    private int xOffset, yOffset;

    private TextAlignment(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public int translateX(int width, String text) {
        int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        return MathHelper.floor_double(((width * xOffset) / 2)) - MathHelper.floor_double((stringWidth * xOffset) / 2);
    }

    @Override
    public int translateY(int height, String text) {
        int stringHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        return MathHelper.floor_double(((height * yOffset) / 2)) - MathHelper.floor_double(((stringHeight * yOffset) / 2));
    }
}
