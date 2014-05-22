package net.gtn.dimensionalpocket.common.core.pocket;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.server.MinecraftServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PocketConfig {

    private static final String backLinkFile = "teleportRegistry";
    private static final String currentChunkFile = "currentChunk";

    /**
     * Adds .json to it.
     * 
     * @param fileName
     * @return
     */
    private static File getConfig(String fileName) throws IOException {
        MinecraftServer server = MinecraftServer.getServer();
        StringBuilder pathName = new StringBuilder();

        if (server.isSinglePlayer())
            pathName.append("saves/");

        pathName.append(server.getFolderName());
        pathName.append("/dimpockets/");
        pathName.append(fileName);
        pathName.append(".json");

        File savefile = server.getFile(pathName.toString());
        if (!savefile.exists()) {
            savefile.getParentFile().mkdirs();
            savefile.createNewFile();
        }
        return savefile;
    }

    public static void saveBackLinkMap(Map<CoordSet, Pocket> backLinkMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File registryFile = getConfig(backLinkFile);

            Pocket[] tempArray = backLinkMap.values().toArray(new Pocket[0]);

            FileWriter writer = new FileWriter(registryFile);
            gson.toJson(tempArray, writer);
            writer.close();

        } catch (Exception e) {
            DPLogger.severe(e);
        }
    }

    public static Map<CoordSet, Pocket> loadBackLinkMap() {
        Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();
        Gson gson = new Gson();

        try {
            File registryFile = getConfig(backLinkFile);

            final FileReader reader = new FileReader(registryFile);

            Pocket[] tempArray = gson.fromJson(reader, Pocket[].class);
            reader.close();

            if (tempArray != null)
                for (Pocket link : tempArray)
                    backLinkMap.put(link.getChunkCoords(), link);

        } catch (Exception e) {
            DPLogger.severe(e);
        }

        return backLinkMap;
    }

    public static void saveCurrentChunk(CoordSet currentChunk) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File registryFile = getConfig(currentChunkFile);

            FileWriter writer = new FileWriter(registryFile);
            gson.toJson(currentChunk, writer);
            writer.close();

        } catch (Exception e) {
            DPLogger.severe(e);
        }
    }

    public static CoordSet loadCurrentChunk() {
        Gson gson = new Gson();

        try {
            File registryFile = getConfig(currentChunkFile);

            final FileReader reader = new FileReader(registryFile);

            CoordSet tempSet = gson.fromJson(reader, CoordSet.class);
            reader.close();

            if (tempSet == null)
                tempSet = new CoordSet(0, 0, 0);

            return tempSet;
        } catch (Exception e) {
            DPLogger.severe(e);
        }
        return new CoordSet(0, 0, 0);
    }

}
