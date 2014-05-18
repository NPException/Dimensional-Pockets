package net.gtn.dimensionalpocket.common.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import com.google.common.collect.ImmutableSet;

public class ChunkLoaderHandler implements LoadingCallback {

    public static final Map<Pocket, Ticket> ticketMap = new HashMap<Pocket, Ticket>();

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        for (Ticket ticket : tickets) {
            if (ticket == null)
                continue;
            CoordSet chunkSet = CoordSet.readFromNBT(ticket.getModData());
            Pocket pocket = PocketRegistry.getPocket(chunkSet);

            if (pocket != null && pocket.isSourceBlockPlaced()) {
                Ticket tempTicket = ticketMap.get(pocket);
                if (tempTicket != null)
                    ForgeChunkManager.releaseTicket(tempTicket);

                ticketMap.put(pocket, ticket);
                ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkSet.getX(), chunkSet.getZ()));
            } else {
                ForgeChunkManager.releaseTicket(ticket);
            }
        }
    }

    public static void addPocketChunkToLoader(World world, Pocket pocket) {
        if (pocket == null)
            return;

        CoordSet chunkSet = pocket.getChunkCoords();
        ChunkCoordIntPair chunkPair = new ChunkCoordIntPair(chunkSet.getX(), chunkSet.getZ());

        if (isChunkLoadedAlready(pocket, chunkPair))
            return;

        Ticket ticket = ticketMap.get(pocket);
        if (ticket == null)
            ticket = ForgeChunkManager.requestTicket(DimensionalPockets.instance, world, ForgeChunkManager.Type.NORMAL);

        if (ticket != null) {
            NBTTagCompound tag = ticket.getModData();
            pocket.getChunkCoords().writeToNBT(tag);
            ForgeChunkManager.forceChunk(ticket, chunkPair);
        }

        ticketMap.put(pocket, ticket);
    }

    public static void removePocketChunkFromLoader(Pocket pocket) {
        if (!ticketMap.containsKey(pocket))
            return;

        Ticket ticket = ticketMap.get(pocket);
        ForgeChunkManager.releaseTicket(ticket);
        ticketMap.remove(pocket);
    }

    private static boolean isChunkLoadedAlready(Pocket pocket, ChunkCoordIntPair pocketPair) {
        if (ticketMap.containsKey(pocket))
            return true;

        for (Map.Entry<Pocket, Ticket> set : ticketMap.entrySet())
            if (set != null && set.getValue() != null) {
                ImmutableSet<ChunkCoordIntPair> loadedChunks = set.getValue().getChunkList();
                if (loadedChunks != null && set.getValue().world.provider.dimensionId == Reference.DIMENSION_ID)
                    for (ChunkCoordIntPair chunkPair : loadedChunks)
                        if (chunkPair.equals(pocketPair))
                            return true;

            }
        return false;
    }
}
