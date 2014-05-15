package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    public PocketTeleporter(WorldServer par1WorldServer) {
        super(par1WorldServer);
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        
        
        
        super.placeInPortal(entity, x, y, z, par8);
    }

}
