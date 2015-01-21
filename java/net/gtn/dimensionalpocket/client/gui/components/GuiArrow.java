package net.gtn.dimensionalpocket.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.gui.components.GuiTexturedButton;

@SideOnly(Side.CLIENT)
public class GuiArrow extends GuiTexturedButton<GuiArrow> {

    public GuiArrow(int x, int y) {
        super(x, y, 0, 180, 18, 10);
    }

    @Override
    public int getTextureXShift(int pass) {
        return 23 * pass;
    }
}
