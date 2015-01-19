package net.gtn.dimensionalpocket.common.core.container;

import me.jezza.oc.client.gui.interfaces.IGuiMessageHandler;
import net.gtn.dimensionalpocket.common.core.pocket.FlowState;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class ContainerPocketConfig extends ContainerAbstract implements IGuiMessageHandler {

    private Pocket pocket;

    public ContainerPocketConfig(TileDimensionalPocket tile) {
        super(tile);
        pocket = tile.getPocket();
    }

    @Override
    public void onClientClick(EntityPlayer player, int id, int process) {
        switch (id) {
            case -1:
                pocket.resetFlowStates();
                break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                pocket.setFlowState(ForgeDirection.VALID_DIRECTIONS[id], FlowState.values()[process]);
                break;
        }
    }
}
