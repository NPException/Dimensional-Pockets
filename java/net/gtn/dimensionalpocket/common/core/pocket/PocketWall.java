package net.gtn.dimensionalpocket.common.core.pocket;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PocketWall {

    private ForgeDirection direction;

    public PocketWall(ForgeDirection direction) {
        this.direction = direction;
    }

    public void setRedstoneStrength(World world, int strength) {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                
            }
    }

    public int getHighestRedstoneStrength(World world) {
        return 0;
    }

}
