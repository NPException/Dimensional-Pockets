package net.gtn.dimensionalpocket.common.core.pocket.states;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.states.RedstoneStateHandler.RedstoneSideState;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.RedstoneHelper;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneStateHandler implements IPocketState {

    private RedstoneState[] redstoneStateArray;

    public RedstoneStateHandler() {
        redstoneStateArray = new RedstoneState[6];
        redstoneStateArray[0] = new RedstoneState();
        redstoneStateArray[1] = new RedstoneState();
        redstoneStateArray[2] = new RedstoneState();
        redstoneStateArray[3] = new RedstoneState();
        redstoneStateArray[4] = new RedstoneState();
        redstoneStateArray[5] = new RedstoneState();
    }

    @Override
    public void onSideChange(Pocket pocket, TileDimensionalPocket tile, CoordSet coordSet, Block block) {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {

            RedstoneState redstoneState = redstoneStateArray[direction.ordinal()];
            if (redstoneState.isOutput())
                return;

            int strength = RedstoneHelper.getCurrentOutput(tile.getWorldObj(), tile.getCoordSet(), direction);

            if (redstoneState.setStrength(strength, RedstoneSideState.INPUT))
                pocket.forcePocketSideUpdate(direction);
        }

    }

    @Override
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction, CoordSet coordSet, Block block) {

    }

    public int getStrength(int side, RedstoneSideState state) {
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UNKNOWN)
            return 0;

        RedstoneState redstoneState = redstoneStateArray[side];
        if (redstoneState.isState(state))
            return redstoneState.getStrength();
        return 0;
    }

    private static class RedstoneState {

        private RedstoneSideState sideState;
        private int strength = 0;

        public RedstoneState() {
            setUnused();
        }

        public int getStrength() {
            return strength;
        }

        public boolean setStrength(int strength, RedstoneSideState state) {
            this.strength = strength;
            if (state == null || strength <= 0) {
                setUnused();
                return true;
            }
            sideState = state;
            return false;
        }

        public void setUnused() {
            sideState = RedstoneSideState.UNUSED;
            strength = 0;
        }

        public boolean isState(RedstoneSideState state) {
            return sideState == state;
        }

        public boolean isUnused() {
            return sideState == RedstoneSideState.UNUSED;
        }

        public boolean isInput() {
            return sideState == RedstoneSideState.INPUT;
        }

        public boolean isOutput() {
            return sideState == RedstoneSideState.OUTPUT;
        }
    }

    public static enum RedstoneSideState {
        UNUSED, INPUT, OUTPUT;
    }
}
