package net.gtn.dimensionalpocket.common.core.sidestates;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneState implements ISideState {

    private int[] signals = new int[6];

    public RedstoneState() {
    }

    @Override
    public void onSideChange(Pocket pocket, TileDimensionalPocket tile) {
        DPLogger.info("CALLED FROM THE MAIN BLOCK");
    }

    @Override
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction) {
        DPLogger.info("CALLED FROM THE POCKET");
    }

    public int getSignal(int side) {
        signals[side] = 15;
        return signals[side];
    }

}
