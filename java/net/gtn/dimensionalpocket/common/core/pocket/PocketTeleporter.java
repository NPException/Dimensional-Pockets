package net.gtn.dimensionalpocket.common.core.pocket;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;
    float spawnYaw, spawnPitch;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet, float spawnYaw, float spawnPitch) {
        super(worldServer);
        Utils.enforceServer();
        this.targetSet = targetSet.copy();
        this.spawnYaw = spawnYaw;
        this.spawnPitch = spawnPitch;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;

        double posX = targetSet.x + 0.5F;
        double posY = targetSet.y + 1;
        double posZ = targetSet.z + 0.5F;

        player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, spawnYaw, spawnPitch);
        player.fallDistance = 0;
    }

    public static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet, float spawnYaw, float spawnPitch) {
        return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet, spawnYaw, spawnPitch);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
}
