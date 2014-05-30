package net.gtn.dimensionalpocket.common.core.sidestates;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraftforge.common.util.ForgeDirection;

public interface ISideState {

    /**
     * Neighbour block change that this sideState is embedded on.
     * 
     * @param pocket
     * @param tile
     */
    public void onSideChange(Pocket pocket, TileDimensionalPocket tile);

    /**
     * Called when a frame block's neighbour changed.
     * 
     * @param pocket
     * @param tile
     */
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction, CoordSet coordSet);

}
