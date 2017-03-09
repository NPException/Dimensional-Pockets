package net.gtn.dimensionalpocket.oc.client.gui.components;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.common.utils.MathHelper;

/**
 * If you do instantiate this class, make sure to pass the class to the super.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiCycle<T extends GuiCycle> extends GuiTexturedButton<T> {

    public int typeState;
    private int srcX, srcY;

    public GuiCycle(int x, int y, int u, int v, int width, int height) {
        super(x, y, u, v, width, height);
        srcX = u;
        srcY = v;
    }

    public T setTypeState(int typeState) {
        this.typeState = typeState;
        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
        return (T) this;
    }

    public int getCurrentTypeState() {
        return typeState;

    }

    /**
     * Will wrap the int afterwards, but not continuously.
     * EG: typeState = 5, but can only exist as a 0 or a 1.
     * typeState = 5, > 1
     * typeState = 0
     * Same in reverse.
     */
    public void processMouseClick(int mouseClick) {
        switch (mouseClick) {
            case 0:
                typeState++;
                break;
            case 1:
                typeState--;
                break;
            case 2:
                typeState = 0;
                break;
        }
        typeState = MathHelper.wrapInt(typeState, getNumberOfStates() - 1);

        u = srcX + getShiftedX(typeState);
        v = srcY + getShiftedY(typeState);
    }

    public abstract int getNumberOfStates();

    public int getShiftedX(int typeState) {
        return 0;
    }

    public int getShiftedY(int typeState) {
        return 0;
    }

}
