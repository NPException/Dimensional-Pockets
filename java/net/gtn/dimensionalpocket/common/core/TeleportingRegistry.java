package net.gtn.dimensionalpocket.common.core;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;

public class TeleportingRegistry {

    private static final int MAX_HEIGHT = 16;
    private static CoordSet currentChunk = new CoordSet(0, 0, 0);

    /**
     * NPE you probably want to fix this.
     * NPE you probably want to fix this.
     * NPE you probably want to fix this.
     * NPE you probably want to fix this.
     * NPE you probably want to fix this.
     */
    public static CoordSet genNewChunkSet() {
        currentChunk.addY(1);
        if (currentChunk.getY() == MAX_HEIGHT) {
            currentChunk.setY(0);
            currentChunk.addX(1);
            if (currentChunk.getX() % 16 == 0) {
                currentChunk.setX(0);
                currentChunk.addZ(1);
            }
        }
        return currentChunk;
    }
}
