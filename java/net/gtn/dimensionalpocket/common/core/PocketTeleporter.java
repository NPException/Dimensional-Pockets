package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    public PocketTeleporter(WorldServer worldServer) {
        super(worldServer);
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {

        DPLogger.info("Called");

        super.placeInPortal(entity, x, y, z, par8);
    }

}
