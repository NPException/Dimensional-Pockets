package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet) {
        super(worldServer);
        this.targetSet = targetSet;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        
        if (! (entity instanceof EntityPlayerMP)) return;
        
        EntityPlayerMP player = (EntityPlayerMP) entity;
        
        double posX = targetSet.getX() * 16;
        double posY = targetSet.getY() * 16;
        double posZ = targetSet.getZ() * 16;

        player.playerNetServerHandler.setPlayerLocation(posX + 8, posY + 1, posZ + 8, player.rotationYaw, player.rotationPitch);
    }

}
