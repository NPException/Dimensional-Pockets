package net.gtn.dimensionalpocket.client.gui.framework;

public abstract class GuiBoolean extends GuiCycle {

    public GuiBoolean(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
    }

    @Override
    public int getNumberOfStates() {
        return 2;
    }

}
