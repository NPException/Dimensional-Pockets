package net.gtn.dimensionalpocket.common.core.teleport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.gtn.dimensionalpocket.common.core.DPLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TeleportingRegistry {

    // map of the format <dimensionalPocketCoords, link>
    private static Map<CoordSet, TeleportLink> backLinkMap = new HashMap<CoordSet, TeleportLink>();

    private static Type backLinkMapType = new TypeToken<Map<CoordSet, TeleportLink>>() {
    }.getType();

    private static final int MAX_HEIGHT = 16;
    // TODO Record this value. We don't want to gen a new set.
    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    public static TeleportLink getLinkForPocketChunkCoords(CoordSet pocketChunkCoords) {
        return backLinkMap.get(pocketChunkCoords);
    }

    public static CoordSet genNewTeleportLink(int dimID, CoordSet coordSet) {
        if (currentChunk.getY() == MAX_HEIGHT) {
            currentChunk.setY(0);
            currentChunk.addX(1);
            // We don't need that, we are just giving out the coordsets in one line.
            // I don't think there will ever be enough pockets on a server to reach
            // the "jittery" coordinate range in that dimension
            // if (currentChunk.getX() % MAX_HEIGHT == 0) {
            // currentChunk.setX(0);
            // currentChunk.addZ(1);
            // if (currentChunk.getZ() % MAX_HEIGHT == 0)
            // currentChunk.setZ(0);
            // }
        }

        TeleportLink link = new TeleportLink(dimID, coordSet, currentChunk.copy());
        backLinkMap.put(link.getPocketChunkCoords(), link);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);

        return link.getPocketChunkCoords();
    }

    public static void changeTeleportLink(CoordSet pocketChunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
        TeleportLink link = backLinkMap.get(pocketChunkCoords);
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
        Gson gson = new Gson();

        try {
            File registryFile = getOrCreateSaveFile();

            JsonWriter writer = new JsonWriter(new FileWriter(registryFile));
            gson.toJson(backLinkMap, backLinkMapType, writer);
            writer.close();
        } catch (IOException e) {
            DPLogger.severe(e);
        }
    }

    public static void loadBackLinkMap() {
        File registryFile = null;
        try {
            registryFile = getOrCreateSaveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (registryFile != null)
            DPLogger.info(registryFile.toPath());
        Gson gson = new Gson();

        try {
            File registryFile = getOrCreateSaveFile();
            JsonReader reader = new JsonReader(new FileReader(registryFile));
            backLinkMap = gson.fromJson(reader, backLinkMapType);
            if (backLinkMap == null) {
                backLinkMap = new HashMap<CoordSet, TeleportLink>();
            }
        } catch (IOException e) {
            DPLogger.severe(e);
        }
    }
}
