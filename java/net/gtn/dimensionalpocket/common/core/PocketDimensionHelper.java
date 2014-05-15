package net.gtn.dimensionalpocket.common.core;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;

public class PocketDimensionHelper {
    public static double[] getRelativeBlockCoords(CoordSet coordSet) {
        double[] tempLoc = new double[3];

        tempLoc[0] = coordSet.getX() * 16;
        tempLoc[1] = coordSet.getX() * 16;
        tempLoc[2] = coordSet.getX() * 16;

        return tempLoc;
    }

    public static void teleportPlayerToPocket(EntityPlayer player) {
        if (player.worldObj.isRemote)
            return;

        int dimID = player.dimension;

        // if (dimID != Reference.DIMENSION_ID)
        transferPlayerToDimension((EntityPlayerMP) player, Reference.DIMENSION_ID, new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID)));
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
}
