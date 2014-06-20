package net.gtn.dimensionalpocket.common.core.pocket.states.redstone;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;

public class RedstoneState {

    private RedstoneSideState sideState;
    private int strength = 0;

    public RedstoneState() {
        setUnused();
    }

    public int getStrength() {
        return strength;
    }

    public boolean setStrength(int strength, RedstoneSideState state) {
        boolean flag = this.strength != strength;
        if (isValid(state)) {
            if (flag) {
                this.strength = strength;
                sideState = state;
                flag = true;
            }
        } else {
            setUnused();
            flag = false;
        }
        return flag;
    }

    public void setUnused() {
        sideState = RedstoneSideState.UNUSED;
        strength = 0;
    }

    public boolean isValid(RedstoneSideState state) {
        return sideState != null && (sideState == RedstoneSideState.UNUSED || sideState == state);
    }

    public boolean isState(RedstoneSideState state) {
        if (state == RedstoneSideState.INPUT)
            return isInput();
        if (state == RedstoneSideState.OUTPUT)
            return isOutput();
        return isUnused();
    }

    public boolean isUnused() {
        boolean flag = sideState == RedstoneSideState.UNUSED;
        if (flag)
            strength = 0;
        return flag;
    }

    public boolean isInput() {
        if (sideState == RedstoneSideState.INPUT && strength == 0)
            setUnused();
        return sideState == RedstoneSideState.INPUT;
    }

    public boolean isOutput() {
        if (sideState == RedstoneSideState.OUTPUT && strength == 0)
            setUnused();
        return sideState == RedstoneSideState.OUTPUT;
    }
}
