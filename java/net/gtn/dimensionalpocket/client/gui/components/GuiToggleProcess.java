package net.gtn.dimensionalpocket.client.gui.components;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.client.gui.framework.GuiCycle;

public class GuiToggleProcess extends GuiCycle {

    public GuiToggleProcess(int x, int y, int u) {
        super(x, y, u, 0, 16, 16);
    }

    @Override
    public void postRender(int mouseX, int mouseY) {
        if (isHoveringOver(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            switch (typeState) {
                case 0:
                    list.add("Disabled");
                    break;
                case 1:
                    list.add("Input");
                    break;
                case 2:
                    list.add("Output");
                    break;
            }
            renderHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    public int getShiftedX(int typeState) {
        return typeState * 16;
    }

    @Override
    public int getNumberOfStates() {
        return 3;
    }

}
