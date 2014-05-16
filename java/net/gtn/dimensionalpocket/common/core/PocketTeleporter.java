package net.gtn.dimensionalpocket.common.core;

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

        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        int index = 0;

        while (!(isAirBlocks(world, targetSet)))
            targetSet.addCoordSet(getRelativeTries(index++));

        double posX = targetSet.getX();
        double posY = targetSet.getY() + 1;
        double posZ = targetSet.getZ();

        if (teleportType.isInward()) {
            posX = posX * 16 + 8;
            posY = (posY * 16) + 1;
            posZ = posZ * 16 + 8;
        }

        player.playerNetServerHandler.setPlayerLocation(posX + 0.5, posY, posZ + 0.5, player.rotationYaw, player.rotationPitch);
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
        //@formatter:off
        INTERNAL(0),
        OUTWARD(1),
        INWARD(2);
        //@formatter:on

        int type = 0;

        private TeleportType(int type) {
            this.type = type;
        }

        public boolean isInternal() {
            return type == 0;
        }

        public boolean isOutward() {
            return type == 1;
        }

        public boolean isInward() {
            return type == 2;
        }

    }

}
