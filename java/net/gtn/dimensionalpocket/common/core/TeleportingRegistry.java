package net.gtn.dimensionalpocket.common.core;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TeleportingRegistry {

    private static HashMap<Integer, HashSet<CoordSet>> tileMap = new HashMap<Integer, HashSet<CoordSet>>();

    private static final int MAX_HEIGHT = 16;
    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    public static CoordSet genNewChunkSet(int dimID, CoordSet coordSet) {
        currentChunk.addY(1);
        if (currentChunk.getY() == MAX_HEIGHT) {
            currentChunk.setY(0);
            currentChunk.addX(1);
            // We don't need that, we are just giving out the coordsets in one line.
            // I don't think there will ever be enough pockets on a server to reach
            // the "jittery" coordinate range in that dimension
//            if (currentChunk.getX() % MAX_HEIGHT == 0) {
//                currentChunk.setX(0);
//                currentChunk.addZ(1);
//                if (currentChunk.getZ() % MAX_HEIGHT == 0)
//                    currentChunk.setZ(0);
//            }
        }

        confirmDim(dimID);
        tileMap.get(dimID).add(coordSet);

        return currentChunk.copy();
    }

    private static void confirmDim(int dimID) {
        if (!tileMap.containsKey(dimID))
            tileMap.put(Integer.valueOf(dimID), new HashSet<CoordSet>());
    }
}
