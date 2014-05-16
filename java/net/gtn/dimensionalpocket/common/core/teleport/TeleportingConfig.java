package net.gtn.dimensionalpocket.common.core.teleport;

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

public class TeleportingConfig {

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

    public static void saveBackLinkMap(Map<CoordSet, Pocket> backLinkMap) {
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

    public static void loadBackLinkMap(Map<CoordSet, Pocket> backLinkMap) {
        Gson gson = new Gson();

        try {
            File registryFile = getOrCreateSaveFile();

            final FileReader reader = new FileReader(registryFile);

            Pocket[] tempArray = gson.fromJson(reader, Pocket[].class);

            reader.close();

            if (backLinkMap == null)
                backLinkMap = new HashMap<CoordSet, Pocket>();

            for (Pocket link : tempArray)
                backLinkMap.put(link.getChunkCoords(), link);

        } catch (Exception e) {
            DPLogger.severe(e);
        }
    }
}
