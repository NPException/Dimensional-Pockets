package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.gtn.dimensionalpocket.common.core.IBlockNotifier;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    CoordSet chunkSet = new CoordSet(0, 0, 0);
    boolean hasChunkSet = false;

    @Override
    public void onBlockPlaced() {

    }

    @Override
    public void onBlockDestroyed() {

    }

    public void genChunkSet() {

    }

    public boolean hasChunkSet() {
        return hasChunkSet;
    }

}
