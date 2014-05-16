package net.gtn.dimensionalpocket.common.core.teleport;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;
    TeleportType teleportType;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet, TeleportType teleportType) {
        super(worldServer);
        this.targetSet = targetSet.copy();
        this.teleportType = teleportType;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        DPLogger.info(teleportType.name());
        DPLogger.info(targetSet);

        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        if (teleportType == TeleportType.INTERNAL) {
            int index = 0;

            while (!(isAirBlocks(world, targetSet)))
                targetSet.addCoordSet(getRelativeTries(index++));
        }

        double posX = targetSet.getX();
        double posY = targetSet.getY();
        double posZ = targetSet.getZ();

        if (teleportType == TeleportType.INTERNAL || teleportType == TeleportType.INWARD) {
            posX = (posX * 16) + 7.5F;
            posY = (posY * 16);
            posZ = (posZ * 16) + 7.5F;
        }

        player.playerNetServerHandler.setPlayerLocation(posX + 0.5, posY + 1, posZ + 0.5, player.rotationYaw, player.rotationPitch);
    }

    private boolean isAirBlocks(World world, CoordSet airSet) {
        return (world.isAirBlock(airSet.getX(), airSet.getY(), airSet.getZ()) && world.isAirBlock(airSet.getX(), airSet.getY() + 1, airSet.getZ()));
    }

    private CoordSet getRelativeTries(int tryCount) {
        if (tryCount == 0)
            return new CoordSet(0, -2, 0);

        int index = 0;

        for (int i = -1; i <= 1; i++)
            for (int j = -2; j <= 2; j++)
                for (int k = -1; k <= 1; k++)
                    if (++index == tryCount)
                        return new CoordSet(i, j, k);
        return null;
    }

    public static enum TeleportType {
        INTERNAL, EXTERNAL, INWARD, OUTWARD;
    }
}
