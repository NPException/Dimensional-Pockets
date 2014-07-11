package net.gtn.dimensionalpocket.client.gui;

import net.gtn.dimensionalpocket.common.core.container.ConfigContainer;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiConfig extends GuiContainer {

    public GuiConfig(TileDimensionalPocket tile) {
        super(new ConfigContainer(tile));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

    }

}
