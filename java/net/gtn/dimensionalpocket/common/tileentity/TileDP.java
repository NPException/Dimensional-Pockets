package net.gtn.dimensionalpocket.common.tileentity;

import me.jezza.oc.common.tile.TileAbstract;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.minecraft.entity.player.EntityPlayer;

public abstract class TileDP extends TileAbstract {

    public boolean isUsableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    public abstract Pocket getPocket();
}
