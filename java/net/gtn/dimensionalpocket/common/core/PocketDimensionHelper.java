package net.gtn.dimensionalpocket.common.core;

import cpw.mods.fml.common.FMLCommonHandler;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.teleport.PocketTeleporter;
import net.gtn.dimensionalpocket.common.core.teleport.Pocket;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.core.teleport.PocketTeleporter.TeleportType;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class PocketDimensionHelper {

    public static void teleportPlayerToPocket(EntityPlayer entityPlayer, CoordSet chunkSet) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;

        PocketTeleporter teleporter = createTeleporter(dimID, chunkSet, TeleportType.INWARD);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }

        generatePocketIfNecessary(player.worldObj, chunkSet);
    }

    private static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet, TeleportType teleportType) {
        return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet, teleportType);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }

    public static void teleportPlayerFromPocket(EntityPlayer entityPlayer, CoordSet frameCoords) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        Pocket link = TeleportingRegistry.getLinkForPocketChunkCoords(frameCoords.copyDividedBy16());

        if (link == null)
            return;

        int dimID = link.getBlockDim();

        PocketTeleporter teleporter = createTeleporter(dimID, link.getBlockCoords(), TeleportType.OUTWARD);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, dimID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }
    }
}
