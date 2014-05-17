package net.gtn.dimensionalpocket.common.core.pocket;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PocketWall {

    private ForgeDirection direction;
    private Pocket pocket;

    public PocketWall(ForgeDirection direction, Pocket pocket) {
        this.direction = direction;
        this.pocket = pocket;
    }

    public void setRedstoneStrength(World world, int strength) {
        CoordSet coordSet = pocket.getChunkCoords().toBlockCoords();

        
        
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {

            }
    }

    public int getHighestRedstoneStrength(World world) {
        return 0;
    }

}
