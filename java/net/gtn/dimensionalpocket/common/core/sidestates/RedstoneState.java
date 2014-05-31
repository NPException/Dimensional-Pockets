package net.gtn.dimensionalpocket.common.core.sidestates;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.RedstoneHelper;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneState implements ISideState {

    private int[] inputSignals = new int[6];
    private int[] outputSignals = new int[6];

    public RedstoneState() {
    }

    @Override
    public void onSideChange(Pocket pocket, TileDimensionalPocket tile) {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            CoordSet blockSet = tile.getCoordSet();
            int strength = RedstoneHelper.getCurrentBlockOuputStrength(tile.getWorldObj(), blockSet.getX(), blockSet.getY(), blockSet.getZ(), direction);
            if (strength != inputSignals[direction.ordinal()]) {
                inputSignals[direction.ordinal()] = strength;
                pocket.forcePocketSideUpdate(direction);
                DPLogger.info(inputSignals[direction.ordinal()]);
            }
        }
    }

    @Override
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction, CoordSet coordSet) {

    }

    public int getInputSignal(int side) {
        return inputSignals[side];
    }

    public int getOutputSignal(int side) {
        return outputSignals[side];
    }

}
