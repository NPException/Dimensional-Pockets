package net.gtn.dimensionalpocket.common.tileentity;

import me.jezza.oc.common.tile.TileAbstract;
import net.minecraft.entity.player.EntityPlayer;

public class TileDP extends TileAbstract {

    public boolean isUsableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

}
