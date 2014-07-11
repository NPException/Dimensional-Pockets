package net.gtn.dimensionalpocket.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiArrow extends GuiCustomButton {

    public static final TextureState state = new TextureState(0, 180, 18, 10);

    public GuiArrow(int x, int y) {
        super(x, y, state.copy());
    }

    public boolean onClick(int mouseX, int mouseY) {
        super.onClick(mouseX, mouseY);
        playButtonClick();
        return clicked;
    }

    @Override
    public int getButtonDelay() {
        return 125;
    }

    @Override
    public int getTextureXShift() {
        return 23;
    }

    @Override
    public int getTextureYShift() {
        return 0;
    }
}
