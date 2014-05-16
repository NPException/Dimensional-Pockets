package net.gtn.dimensionalpocket.common.core.pocket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.server.MinecraftServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PocketRegistry {

    private static Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();

    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    public static Pocket getPocket(CoordSet chunkCoords) {
        if (backLinkMap.containsKey(chunkCoords))
            return backLinkMap.get(chunkCoords);
        return null;
    }

    public static Pocket generateNewPocket(int dimIDSource, CoordSet coordSetSource) {
        if (currentChunk.getY() == 16)
            currentChunk.setY(0).addX(1);

        Pocket link = new Pocket(currentChunk.copy(), dimIDSource, coordSetSource);
        backLinkMap.put(link.getChunkCoords(), link);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);
        return link;
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
