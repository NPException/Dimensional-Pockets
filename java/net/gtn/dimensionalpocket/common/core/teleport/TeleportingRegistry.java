package net.gtn.dimensionalpocket.common.core.teleport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

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
    private static HashMap<CoordSet, HashSet<CoordSet>> tileMap = new HashMap<Integer, HashSet<CoordSet>>();

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
        
        TeleportLink link = new TeleportLink(dimID, coordSet, currentChunk.copy());

        

        return link.getPocketChunkCoords();
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
