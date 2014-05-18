package net.gtn.dimensionalpocket.common.core.pocket;

import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.tileentity.SharedTileDPFrame;

public class PocketRegistry {

    private static Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();

    private static CoordSet currentChunk = new CoordSet(0, 0, 0);
    
    private static SharedTileDPFrame sharedFrameTile;
    
    public static SharedTileDPFrame getSharedFrameTile() {
        if (sharedFrameTile == null)
            sharedFrameTile = new SharedTileDPFrame();
        return sharedFrameTile;           
    }

    public static Pocket getPocket(CoordSet chunkCoords) {
        if (backLinkMap.containsKey(chunkCoords))
            return backLinkMap.get(chunkCoords);
        return null;
    }

    public static Pocket generateNewPocket(int dimIDSource, CoordSet coordSetSource) {
        if (currentChunk.getY() == 16)
            currentChunk.setY(0).addX(1);

        Pocket pocket = new Pocket(currentChunk.copy(), dimIDSource, coordSetSource);
        backLinkMap.put(pocket.getChunkCoords(), pocket);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);
        return pocket;
    }

    public static void updatePocket(CoordSet chunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
        Pocket link = backLinkMap.get(chunkCoords);
        if (link == null) {
            DPLogger.severe("No Pocket for chunkCoords: " + chunkCoords);
            return;
        }

        link.setBlockDim(newBlockDimID);
        link.setBlockCoords(newBlockCoords);
    }

    public static void updatePocketSpawn(CoordSet chunkCoords, CoordSet spawnSet) {
        Pocket link = backLinkMap.get(chunkCoords);
        if (link == null) {
            DPLogger.severe("No Pocket for chunkCoords: " + chunkCoords);
            return;
        }

        link.setSpawnSet(spawnSet);
    }

    public static void saveData() {
        PocketConfig.saveBackLinkMap(backLinkMap);
        PocketConfig.saveCurrentChunk(currentChunk);
    }

    public static void loadData() {
        PocketConfig.loadBackLinkMap(backLinkMap);
        currentChunk = PocketConfig.loadCurrentChunk();
    }
}
