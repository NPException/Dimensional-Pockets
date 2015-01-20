package net.gtn.dimensionalpocket.common.items.handlers;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.pocket.FlowState;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class NetherCrystalHandler extends UsableHandlerAbstract {

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        TileEntity te = coordSet.getTileEntity(world);
    	if (!(te instanceof TileDimensionalPocket))
            return false;

        //player.openGui(DimensionalPockets.instance, 1, world, coordSet.getX(), coordSet.getY(), coordSet.getZ());
        // if (!player.capabilities.isCreativeMode)
        // itemStack.stackSize--;
        if (world.isRemote) {
        	player.swingItem();
            return false;
        }
        
        Pocket pocket = ((TileDimensionalPocket) te).getPocket();
        
        ForgeDirection fdSide = ForgeDirection.getOrientation(side);
        FlowState state = pocket.getFlowState(fdSide);
        int nextStateOrdinal = state.ordinal() + 1;
        if (nextStateOrdinal >= FlowState.values().length)
        	nextStateOrdinal = 0;
        
        FlowState newState = FlowState.values()[nextStateOrdinal];
        pocket.setFlowState(fdSide, newState);
        
        return true;
    }
}
