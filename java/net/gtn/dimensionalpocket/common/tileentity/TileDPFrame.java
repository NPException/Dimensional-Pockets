package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.tileentity.TileEntity;

public class TileDPFrame extends TileEntity {

    public CoordSet getCoordSet() {
        return new CoordSet(xCoord, yCoord, zCoord);
    }

}
