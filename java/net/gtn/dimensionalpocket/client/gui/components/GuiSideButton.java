package net.gtn.dimensionalpocket.client.gui.components;

import me.jezza.oc.client.gui.components.interactions.GuiUntexturedButton;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class GuiSideButton extends GuiUntexturedButton {

    private ForgeDirection direction = ForgeDirection.UNKNOWN;

    public GuiSideButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ForgeDirection getDirection() {
        return direction;
    }

    public GuiSideButton setDirection(ForgeDirection direction) {
        this.direction = direction;
        return this;
    }


    @Override
    public void renderForeground(int mouseX, int mouseY, int translatedX, int translatedY) {
        if (canClick(mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(Utils.capitalizeString(direction.name()));
            renderHoveringText(list, translatedX, translatedY, fontRendererObj);
        }
    }
}
