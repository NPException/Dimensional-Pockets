package net.gtn.dimensionalpocket.common.core.container;

import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ConfigContainer extends Container {

    private TileDimensionalPocket tile;

    public ConfigContainer(TileDimensionalPocket tile) {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

}
