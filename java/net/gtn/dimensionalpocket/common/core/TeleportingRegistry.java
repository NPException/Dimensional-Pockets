package net.gtn.dimensionalpocket.common.core;

import net.minecraft.entity.player.EntityPlayer;

public class TeleportingRegistry {

    public static class PlayerState {
        double x, y, z;
        String username;

        public PlayerState(EntityPlayer player) {
            this.x = player.posX;
            this.y = player.posY;
            this.z = player.posZ;
            this.username = player.getDisplayName();
        }
    }

}
