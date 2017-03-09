package net.gtn.dimensionalpocket.oc.client.gui.components.interactions;

import net.gtn.dimensionalpocket.oc.client.gui.components.GuiCycle;

/**
 * Used to toggle between two states.
 * You can override it and get more than 2 states out of it.
 */
public class GuiToggle extends GuiCycle<GuiToggle> {

    public GuiToggle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
    }

    @Override
    public int getNumberOfStates() {
        return 2;
    }

}
