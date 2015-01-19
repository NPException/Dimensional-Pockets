package net.gtn.dimensionalpocket.client.gui.components;

import me.jezza.oc.client.gui.components.GuiCycle;

import java.util.ArrayList;

public class GuiToggleProcess extends GuiCycle<GuiToggleProcess> {

    public GuiToggleProcess(int x, int y, int u) {
        super(x, y, u, 0, 16, 16);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, int translatedX, int translatedY) {
        if (canClick(mouseX, mouseY)) {
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
            renderHoveringText(list, translatedX, translatedY, fontRendererObj);
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
