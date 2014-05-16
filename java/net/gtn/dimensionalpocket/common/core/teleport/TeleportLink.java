package net.gtn.dimensionalpocket.common.core.teleport;

import java.io.Serializable;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;

public class TeleportLink implements Serializable {
    private int blockDim;
    private CoordSet blockCoords, pocketChunkCoords;

    TeleportLink(int blockDim, CoordSet blockCoords, CoordSet pocketChunkCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
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

    public void setBlockDim(int blockDim) {
        this.blockDim = blockDim;
    }

    public void setBlockCoords(CoordSet blockCoords) {
        this.blockCoords = blockCoords;
    }
}
