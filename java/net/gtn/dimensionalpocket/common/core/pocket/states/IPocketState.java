package net.gtn.dimensionalpocket.common.core.pocket.states;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPocketState {

    /**
     * Neighbour block change that this sideState is embedded on.
     * 
     * @param pocket
     * @param tile
     */
    public void onSideChange(Pocket pocket, TileDimensionalPocket tile, CoordSet coordSet, Block block);

    /**
     * Called when a frame block's neighbour changed.
     * 
     * @param pocket
     * @param tile
     */
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction, CoordSet coordSet, Block block);

}
