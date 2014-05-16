package net.gtn.dimensionalpocket.common.core.teleport;

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

public class TeleportingRegistry {

    private static Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();

    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    public static Pocket getPocket(CoordSet chunkCoords) {
        DPLogger.info(chunkCoords);
        if (backLinkMap.containsKey(chunkCoords))
            return backLinkMap.get(chunkCoords);
        return null;
    }

    public static Pocket generateNewPocket(int dimID, CoordSet coordSet) {
        if (currentChunk.getY() == 16)
            currentChunk.setY(0).addX(1);

        Pocket link = new Pocket(currentChunk.copy(), dimID, coordSet);
        backLinkMap.put(link.getChunkCoords(), link);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);
        return link;
    }

    public static void changePocket(CoordSet chunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
        Pocket link = backLinkMap.get(chunkCoords);
        if (link == null)
            DPLogger.severe("No TeleportLink for pocketChunkCoords: " + chunkCoords);

        link.setBlockDim(newBlockDimID);
        link.setBlockCoords(newBlockCoords);
    }

    public static void saveData() {
        TeleportingConfig.saveBackLinkMap(backLinkMap);
        TeleportingConfig.saveCurrentChunk(currentChunk);
    }

    public static void loadData() {
        TeleportingConfig.loadBackLinkMap(backLinkMap);
        currentChunk = TeleportingConfig.loadCurrentChunk();
    }
}
