package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet coordSet;

    public PocketTeleporter(WorldServer worldServer, CoordSet coordSet) {
        super(worldServer);
        this.coordSet = coordSet;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {

    
        
        
    }

}
