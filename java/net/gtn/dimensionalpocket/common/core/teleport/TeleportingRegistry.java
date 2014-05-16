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

    // map of the format <dimensionalPocketCoords, link>
    private static Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();

    // TODO Record this value. We don't want to gen a new set.
    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    public static Pocket getLinkForPocketChunkCoords(CoordSet pocketChunkCoords) {
        if (backLinkMap.containsKey(pocketChunkCoords))
            return backLinkMap.get(pocketChunkCoords);
        return null;
    }

    public static CoordSet genNewTeleportLink(int dimID, CoordSet coordSet) {
        if (currentChunk.getY() == 16)
            currentChunk.setY(0).addX(1);

        Pocket link = new Pocket(currentChunk.copy(), dimID, coordSet);
        backLinkMap.put(link.getPocketChunkCoords(), link);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);

        return link.getPocketChunkCoords();
    }

    public static void changeTeleportLink(CoordSet pocketChunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
        Pocket link = backLinkMap.get(pocketChunkCoords);
        if (link == null)
            DPLogger.severe("No TeleportLink for pocketChunkCoords: " + pocketChunkCoords);

        link.setBlockDim(newBlockDimID);
        link.setBlockCoords(newBlockCoords);
    }

    private static File getOrCreateSaveFile() throws IOException {
        MinecraftServer server = MinecraftServer.getServer();
        StringBuilder filename = new StringBuilder();

        if (server.isSinglePlayer())
            filename.append("saves/");

        filename.append(server.getWorldName());
        filename.append("/dimpockets/teleportRegistry.json");

        File savefile = server.getFile(filename.toString());
        if (!savefile.exists()) {
            savefile.getParentFile().mkdirs();
            savefile.createNewFile();
        }
        return savefile;
    }

    public static void saveBackLinkMap() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File registryFile = getOrCreateSaveFile();

            Pocket[] tempArray = backLinkMap.values().toArray(new Pocket[0]);

            FileWriter writer = new FileWriter(registryFile);
            gson.toJson(tempArray, writer);
            writer.close();

        } catch (Exception e) {
            DPLogger.severe(e);
        }
    }

    public static void loadBackLinkMap() {
        Gson gson = new Gson();

        try {
            File registryFile = getOrCreateSaveFile();

            final FileReader reader = new FileReader(registryFile);

            Pocket[] tempArray = gson.fromJson(reader, Pocket[].class);

            reader.close();

            if (backLinkMap == null)
                backLinkMap = new HashMap<CoordSet, Pocket>();

            for (Pocket link : tempArray)
                backLinkMap.put(link.getPocketChunkCoords(), link);

        } catch (Exception e) {
            DPLogger.severe(e);
        }
    }
}
