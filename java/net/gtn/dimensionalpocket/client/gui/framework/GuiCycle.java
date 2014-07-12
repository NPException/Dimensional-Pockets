package net.gtn.dimensionalpocket.client.gui.framework;

public abstract class GuiCycle extends GuiTexturedButton {

    public int typeState;

    private int srcX, srcY;

    public GuiCycle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
        srcX = u;
        srcY = v;
    }

    public GuiCycle setTypeState(int typeState) {
        this.typeState = typeState;
        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
        return this;
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int mouseClick) {
        super.onClick(mouseX, mouseY, mouseClick);
        if (clicked) {
            if (mouseClick == 0)
                typeState++;
            if (mouseClick == 1)
                typeState--;
            if (mouseClick == 2)
                typeState = 0;
            if (typeState > getNumberOfStates() - 1)
                typeState = 0;
            if (typeState < 0)
                typeState = getNumberOfStates() - 1;
        }

        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);

        return clicked;
    }

    public abstract int getNumberOfStates();

    public int getShiftedX(int typeState) {
        return 0;
    }

    public int getShiftedY(int typeState) {
        return 0;
    }

}
