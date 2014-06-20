package net.gtn.dimensionalpocket.common.core.pocket.states;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.states.redstone.RedstoneSideState;
import net.gtn.dimensionalpocket.common.core.pocket.states.redstone.RedstoneState;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.RedstoneHelper;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.world.World;
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
        World world = tile.getWorldObj();
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            RedstoneState redstoneState = redstoneStateArray[direction.ordinal()];
            if (redstoneState.isOutput())
                return;

            int strength = RedstoneHelper.getCurrentOutput(world, coordSet, direction);

            if (redstoneState.setStrength(strength, RedstoneSideState.INPUT))
                pocket.forcePocketSideUpdate(direction);
        }
    }

    @Override
    public void onSidePocketChange(Pocket pocket, ForgeDirection direction, CoordSet coordSet, Block block) {
        RedstoneState redstoneState = redstoneStateArray[direction.ordinal()];
        if (redstoneState.isInput())
            return;

        int strength = RedstoneHelper.getCurrentOutput(PocketRegistry.getWorldForPockets(), coordSet, direction.getOpposite());

        if (redstoneState.setStrength(strength, RedstoneSideState.OUTPUT)) {
            if (direction == ForgeDirection.NORTH)
                DPLogger.info(strength);
            pocket.forceSideUpdate(direction);
        }
    }

    public int getOutput(int side) {
        return getStrength(side, RedstoneSideState.OUTPUT);
    }

    public int getInput(int side) {
        return getStrength(side, RedstoneSideState.INPUT);
    }

    private int getStrength(int side, RedstoneSideState state) {
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UNKNOWN)
            return 0;

        RedstoneState redstoneState = redstoneStateArray[side];
        if (redstoneState.isState(state))
            return redstoneState.getStrength();
        return 0;
    }
}
