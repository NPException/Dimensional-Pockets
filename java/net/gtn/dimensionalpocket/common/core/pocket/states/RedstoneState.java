package net.gtn.dimensionalpocket.common.core.pocket.states;

public class RedstoneState {

    private RedstoneSideState sideState = RedstoneSideState.UNUSED;
    private int strength = 0;

    public int getStrength() {
        return strength;
    }

    public boolean setStrength(int strength, RedstoneSideState state) {
        boolean flag = this.strength != strength && isValid(state);

        if (flag) {
            this.strength = strength;
            sideState = state;
        }

        return flag;
    }

    public void setUnused() {
        sideState = RedstoneSideState.UNUSED;
        strength = 0;
    }

    public boolean isValid(RedstoneSideState state) {
        return isUnused() || isState(state);
    }

    public boolean isState(RedstoneSideState state) {
        if (strength == 0)
            setUnused();
        return sideState == state;
    }

    public boolean isUnused() {
        return isState(RedstoneSideState.UNUSED);
    }

    public boolean isInput() {
        return isState(RedstoneSideState.INPUT);
    }

    public boolean isOutput() {
        return isState(RedstoneSideState.OUTPUT);
    }

    public static enum RedstoneSideState {
        UNUSED, INPUT, OUTPUT;
    }
}
