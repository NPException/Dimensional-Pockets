package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.minecraft.tileentity.TileEntity;

public class TileDP extends TileEntity {

    public CoordSet getCoordSet() {
        return new CoordSet(xCoord, yCoord, zCoord);
    }

}
