package net.gtn.dimensionalpocket.common.core.teleport;

import net.gtn.dimensionalpocket.common.core.CoordSet;

public class TeleportLink {
    private int blockDim;
    private CoordSet blockCoords, pocketChunkCoords;
    
    TeleportLink(int blockDim, CoordSet blockCoords, CoordSet pocketChunkCoords) {
        this.blockDim = blockDim;
        this.blockCoords = blockCoords;
        this.pocketChunkCoords = pocketChunkCoords;
    }
    
    public int getBlockDim() {
        return blockDim;
    }
    public CoordSet getBlockCoords() {
        return blockCoords;
    }
    public CoordSet getPocketChunkCoords() {
        return pocketChunkCoords;
    }
}
