package net.gtn.dimensionalpocket.common.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gtn.dimensionalpocket.common.core.teleport.Pocket;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkLoadHandler implements LoadingCallback {

    public static final Map<Pocket, Ticket> ticketMap = new HashMap<Pocket, Ticket>();

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                int x = ticket.getModData().getInteger("xcoord");
                int y = ticket.getModData().getInteger("ycoord");
                int z = ticket.getModData().getInteger("zcoord");
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileDimensionalPocket) {
                    TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
                    Pocket pocket = tile.getPocket();

                    if (ticketMap.containsKey(pocket))
                        ForgeChunkManager.releaseTicket(ticket);

                    ticketMap.put(pocket, ticket);
                    CoordSet chunkSet = pocket.getChunkCoords().copy();
                    ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(par1, par2));
                } else {
                    ForgeChunkManager.releaseTicket(ticket);
                }
            }
        }
    }

    public static void addPocketChunkToLoad(Pocket pocket) {
        if (pocket == null)
            return;

    }

}
