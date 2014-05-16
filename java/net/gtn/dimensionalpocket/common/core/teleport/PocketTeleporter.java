package net.gtn.dimensionalpocket.common.core.teleport;

import cpw.mods.fml.client.ExtendedServerListData;
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

        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        double posX = targetSet.getX();
        double posY = targetSet.getY();
        double posZ = targetSet.getZ();

        // Converting from chunk to blocks.
        if (teleportType == TeleportType.INWARD) {
            posX = (posX * 16) + 8;
            posY = (posY * 16);
            posZ = (posZ * 16) + 8;
        }

        CoordSet airSet = new CoordSet((int) posX, (int) posY, (int) posZ);
        CoordSet tempSet = airSet.copy();

        int index = 0;
        while (!(isAirBlocks(world, tempSet))) {
            tempSet = airSet.copy();
            CoordSet additionSet = getRelativeTries(index++);
            if (additionSet == null)
                teleportType = TeleportType.REBOUND;
            tempSet.addCoordSet(additionSet);
        }

        DPLogger.info(tempSet);

        player.playerNetServerHandler.setPlayerLocation(tempSet.getX() - 0.5F, tempSet.getY() + 1, tempSet.getZ() - 0.5F, player.rotationYaw, player.rotationPitch);
    }

    private boolean isAirBlocks(World world, CoordSet airSet) {
        if (airSet.getY() < 1)
            return false;
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
        INWARD, OUTWARD, REBOUND;
    }
}
