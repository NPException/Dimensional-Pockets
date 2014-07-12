package net.gtn.dimensionalpocket.client.gui.components;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.client.gui.framework.GuiBoolean;

public class GuiStateType extends GuiBoolean {

    public GuiStateType(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
    }

    @Override
    public int getShiftedX(int typeState) {
        return typeState * 16;
    }

    @Override
    public void postRender(int mouseX, int mouseY) {
        if (isHoveringOver(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            if (typeState == 0)
                list.add("Enabled");
            if (typeState == 1)
                list.add("Disabled");
            renderHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
    }

}
