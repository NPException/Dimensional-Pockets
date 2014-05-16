package net.gtn.dimensionalpocket.common.core.teleport;

import java.io.Serializable;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class Pocket implements Serializable {

    private boolean generated = false;
    private int blockDim;
    private CoordSet blockCoords, pocketChunkCoords;

    Pocket(CoordSet pocketChunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        this.pocketChunkCoords = pocketChunkCoords;
    }

    /**
     * Generates the new room. THATS why you hired me :D
     * 
     * @author NPException
     * @param world
     * @param chunkSet
     */
    public void generatePocket(World world) {
        if (generated)
            return;

        int worldX = pocketChunkCoords.getX() * 16;
        int worldY = pocketChunkCoords.getY() * 16;
        int worldZ = pocketChunkCoords.getZ() * 16;

        Chunk chunk = world.getChunkFromChunkCoords(pocketChunkCoords.getX(), pocketChunkCoords.getZ());

        int l = worldY >> 4;
        ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];

        if (extendedBlockStorage == null) {
            extendedBlockStorage = new ExtendedBlockStorage(worldY, !world.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedBlockStorage;
        }

        // FULL GEN AVERAGE TIME: 505052.3125 nanoSeconds
        // EDGED GEN AVERAGE TIME: 318491.4375 nanoSeconds

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    boolean flagX = x == 0 || x == 15;
                    boolean flagY = y == 0 || y == 15;
                    boolean flagZ = z == 0 || z == 15;

                    // Made these flags, so I could add these checks, almost halves it in time.
                    if (!(flagX || flagY || flagZ) || (flagX && (flagY || flagZ)) || (flagY && (flagX || flagZ)) || (flagZ && (flagY || flagX)))
                        continue;

                    extendedBlockStorage.func_150818_a(x, y, z, ModBlocks.dimensionalPocketFrame);

                    world.markBlockForUpdate(worldX + x, worldY + y, worldZ + z);

                    // use that method if setting things in the chunk will cause problems in the future
                    // world.setBlock(worldX+x, worldY+y, worldZ+z, ModBlocks.dimensionalPocketFrame);
                }
            }
        }

        generated = world.getBlock((pocketChunkCoords.getX() * 16) + 1, pocketChunkCoords.getY() * 16, (pocketChunkCoords.getZ() * 16) + 1) == ModBlocks.dimensionalPocketFrame;
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
