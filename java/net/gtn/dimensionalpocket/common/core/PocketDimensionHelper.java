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

    public static void teleportPlayerToPocket(EntityPlayer entityPlayer, CoordSet targetSet) {
        if (entityPlayer.worldObj.isRemote)
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;

        if (dimID != Reference.DIMENSION_ID) {
            PocketTeleporter teleporter = new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), targetSet);
            transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        } else {
            double posX = targetSet.getX() * 16;
            double posY = targetSet.getY() * 16;
            double posZ = targetSet.getZ() * 16;

            player.playerNetServerHandler.setPlayerLocation(posX + 8, posY + 8, posZ + 8, player.rotationYaw, player.rotationPitch);
        }

    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
}
