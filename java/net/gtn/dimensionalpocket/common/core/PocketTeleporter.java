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
        DPLogger.info(teleportType.getType());
        DPLogger.info(targetSet);
        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        if (teleportType.isInternalInward() || teleportType.isInternalOutward()) {
            int index = 0;

            while (!(isAirBlocks(world, targetSet)))
                targetSet.addCoordSet(getRelativeTries(index++));
        }

        double posX = targetSet.getX();
        double posY = targetSet.getY();
        double posZ = targetSet.getZ();

        DPLogger.info(teleportType.isInward() || teleportType.isInternalInward());
        
        if (teleportType.isInward() || teleportType.isInternalInward()) {
            posX = (posX * 16) + 8;
            posY = (posY * 16);
            posZ = (posZ * 16) + 8;
        }

        DPLogger.info(posX + 0.5F);
        DPLogger.info(posY + 1);
        DPLogger.info(posZ + 0.5F);

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
        //@formatter:off
        INTERNAL_INWARD(0),
        INTERNAL_OUTWARD(1),
        OUTWARD(2),
        INWARD(3);
        //@formatter:on

        int type = 0;

        private TeleportType(int type) {
            this.type = type;
        }

        public boolean isInternalInward() {
            return type == 0;
        }

        public boolean isInternalOutward() {
            return type == 1;
        }

        public boolean isOutward() {
            return type == 2;
        }

        public boolean isInward() {
            return type == 3;
        }
        
        public int getType() {
            return type;
        }

    }

}
