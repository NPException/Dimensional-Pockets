package net.gtn.dimensionalpocket.client.gui.components;

import me.jezza.oc.client.gui.components.interactions.GuiToggle;

import java.util.ArrayList;

public class GuiStateType extends GuiToggle {

    public GuiStateType(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
    }

    @Override
    public int getShiftedX(int typeState) {
        return typeState * 16;
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, int translatedX, int translatedY) {
        if (canClick(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            if (typeState == 0)
                list.add("Enabled");
            if (typeState == 1)
                list.add("Disabled");
            renderHoveringText(list, translatedX, translatedY, fontRendererObj);
        }
    }

}
