package net.gtn.dimensionalpocket.common.core.teleport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.gtn.dimensionalpocket.common.core.DPLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TeleportingRegistry {
    

    private static File registryFile = new File("config/dimensionalpockets/teleportregistry.json");

    // map of the format <dimensionalPocketCoords, link>
    private static Map<CoordSet, TeleportLink> backLinkMap = new HashMap<CoordSet, TeleportLink>();

    private static final int MAX_HEIGHT = 16;
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
//            if (currentChunk.getX() % MAX_HEIGHT == 0) {
//                currentChunk.setX(0);
//                currentChunk.addZ(1);
//                if (currentChunk.getZ() % MAX_HEIGHT == 0)
//                    currentChunk.setZ(0);
//            }
        }
        
        TeleportLink link = new TeleportLink(dimID, coordSet, currentChunk.copy());
        backLinkMap.put(link.getPocketChunkCoords(), link);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);

        return link.getPocketChunkCoords();
    }
    
    public static void changeTeleportLink(CoordSet pocketChunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
        TeleportLink link = backLinkMap.get(pocketChunkCoords);
        if (link == null) {
            DPLogger.severe("No TeleportLink for pocketChunkCoords: " + pocketChunkCoords);
        }
        link.setBlockDim(newBlockDimID);
        link.setBlockCoords(newBlockCoords);
    }
    
    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        Gson gson = new Gson();
        
        try {
            if (!registryFile.exists()) {
                registryFile.getParentFile().mkdirs();
                registryFile.createNewFile();
            }

            JsonWriter writer = new JsonWriter(new FileWriter(registryFile));
        } catch (IOException e) {
            DPLogger.severe(e);
        }
    }
}
