package net.gtn.dimensionalpocket.common.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkLoaderHandler implements LoadingCallback {

    private static class TicketWrapper {
        /**
         * pockets rooms belonging to the ticket
         */
        final List<CoordSet> loadedRooms = new ArrayList<CoordSet>();
        Ticket ticket;
    }

    public static final Map<CoordSet, TicketWrapper> ticketMap = new HashMap<CoordSet, TicketWrapper>();

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        for (Ticket ticket : tickets) {
            if (ticket == null)
                continue;

            CoordSet chunkXZSet = CoordSet.readFromNBT(ticket.getModData());
            TicketWrapper wrapper = ticketMap.get(chunkXZSet);

            if (wrapper == null) {
                DPLogger.warning("Ticket from forge contained a chunkXZSet for which no TicketWrapper exists. Ignoring ticket.", ChunkLoaderHandler.class);
                continue;
            }

            if (!wrapper.loadedRooms.isEmpty()) {
                ChunkLoaderHandler.revalidateTicket(chunkXZSet, ticket);
            } else {
                DPLogger.warning("A ticket was active for a chunk without loaded pocket rooms.", ChunkLoaderHandler.class);
                ForgeChunkManager.releaseTicket(ticket);
                wrapper.ticket = null;
            }
        }
    }

    private static void revalidateTicket(CoordSet chunkXZSet, Ticket currentTicket) {
        TicketWrapper wrapper = ticketMap.get(chunkXZSet);

        if (wrapper == null) {
            DPLogger.severe("Can't revalidate ticket! No TicketWrapper in ticketMap for chunkSet: " + chunkXZSet.toString());
            return;
        }

        if (wrapper.ticket != null)
            ForgeChunkManager.releaseTicket(wrapper.ticket);

        wrapper.ticket = currentTicket;

        ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkCoordIntPair(chunkXZSet.getX(), chunkXZSet.getZ()));
    }

    public static void addPocketToChunkLoader(Pocket pocket) {
        if (pocket == null)
            return;

        CoordSet pocketSet = pocket.getChunkCoords();
        CoordSet chunkXZSet = pocketSet.copy().setY(0); // set to 0 to get the same CoordSet for every pocket in the same chunk

        TicketWrapper wrapper = ticketMap.get(chunkXZSet);
        if (wrapper == null) {
            wrapper = new TicketWrapper();
            ticketMap.put(chunkXZSet, wrapper);
        }

        if (!wrapper.loadedRooms.contains(pocketSet)) {
            wrapper.loadedRooms.add(pocketSet);
            DPLogger.info("Marked the following pocket room to the be loaded: " + pocketSet.toString(), ChunkLoaderHandler.class);
        } else {
            DPLogger.warning("The following Pocket was already marked as loaded: " + pocketSet.toString(), ChunkLoaderHandler.class);
        }

        refreshWrapperTicket(chunkXZSet);
    }

    private static void refreshWrapperTicket(CoordSet chunkXZSet) {
        TicketWrapper wrapper = ticketMap.get(chunkXZSet); // wrapper should never be null at this point. If it is, we deserve the error!

        if (wrapper.ticket == null) {
            World world = PocketRegistry.getWorldForPockets();
            wrapper.ticket = ForgeChunkManager.requestTicket(DimensionalPockets.instance, world, ForgeChunkManager.Type.NORMAL);

            if (wrapper.ticket != null) {
                NBTTagCompound tag = wrapper.ticket.getModData();
                chunkXZSet.writeToNBT(tag);
                ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkCoordIntPair(chunkXZSet.getX(), chunkXZSet.getZ()));
            } else {
                DPLogger.warning("No new tickets available from the ForgeChunkManager.", ChunkLoaderHandler.class);
            }
        }
    }

    public static void removePocketFromChunkLoader(Pocket pocket) {
        CoordSet pocketSet = pocket.getChunkCoords();
        CoordSet chunkXZSet = pocketSet.copy().setY(0);

        if (!ticketMap.containsKey(chunkXZSet)) {
            DPLogger.warning("Something tried to remove a loaded pocket from a chunk that was never loaded before...");
            return;
        }

        TicketWrapper wrapper = ticketMap.get(chunkXZSet);

        if (wrapper.loadedRooms.remove(pocketSet))
            DPLogger.info("Removed the following pocket room from the list of loaded rooms: " + pocketSet.toString(), ChunkLoaderHandler.class);
        else
            DPLogger.warning("The following pocket room wanted to be removed, but was not marked as loaded: " + pocketSet.toString(), ChunkLoaderHandler.class);

        if (wrapper.loadedRooms.isEmpty()) {
            ForgeChunkManager.releaseTicket(wrapper.ticket);
            wrapper.ticket = null;
        }
    }

}
