package net.gtn.dimensionalpocket.common.core.pocket;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet) {
        super(worldServer);
        this.targetSet = targetSet.copy();
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        double posX = targetSet.getX() + 0.5F;
        double posY = targetSet.getY() + 1;
        double posZ = targetSet.getZ() + 0.5F;

        player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
        player.fallDistance = 0;
    }

    public static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet) {
        return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
}
