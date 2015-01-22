package net.gtn.dimensionalpocket.common.core.pocket;

import java.util.HashMap;
import java.util.Map;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PocketRegistry {

    private static Map<CoordSet, Pocket> backLinkMap = new HashMap<CoordSet, Pocket>();

    private static CoordSet currentChunk = new CoordSet(-100, 0, 0);

    public static WorldServer getWorldForPockets() {
        return MinecraftServer.getServer().worldServerForDimension(Reference.DIMENSION_ID);
    }

    public static Pocket getPocket(CoordSet chunkCoords) {
        if (backLinkMap.containsKey(chunkCoords))
            return backLinkMap.get(chunkCoords);
        return null;
    }

    public static Pocket getOrCreatePocket(World world, CoordSet coordSetSource) {
        Utils.enforceServer();
        
        int dimIDSource = world.provider.dimensionId;
        for (Pocket pocket : backLinkMap.values())
            if (pocket.getBlockDim() == dimIDSource && pocket.getBlockCoords().equals(coordSetSource))
                return pocket;

        if (currentChunk.getY() >= 16)
            currentChunk.setY(0).addX(1);

        Pocket pocket = new Pocket(currentChunk.copy(), dimIDSource, coordSetSource);
        backLinkMap.put(pocket.getChunkCoords(), pocket);

        // add one here, so we start at 0 with the first room
        currentChunk.addY(1);

        saveData();

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

        saveData();
    }

    public static void updatePocketSpawn(CoordSet chunkCoords, CoordSet spawnCoords) {
        Pocket link = backLinkMap.get(chunkCoords);
        if (link == null) {
            DPLogger.severe("No Pocket for chunkCoords: " + chunkCoords);
            return;
        }

        link.setSpawnCoords(spawnCoords);

        saveData();
    }

    public static void saveData() {
        PocketConfig.saveBackLinkMap(backLinkMap);
        PocketConfig.saveCurrentChunk(currentChunk);
    }

    public static void loadData() {
        backLinkMap = PocketConfig.loadBackLinkMap();
        currentChunk = PocketConfig.loadCurrentChunk();
    }
    
    public static void initChunkLoading() {
        for (Pocket pocket : backLinkMap.values())
            if (pocket.isSourceBlockPlaced())
                ChunkLoaderHandler.addPocketToChunkLoader(pocket);
    }
}
