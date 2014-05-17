package net.gtn.dimensionalpocket.common.core.utils;

import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class TeleportPreparation {
    public enum Direction {
        INTO_POCKET,
        OUT_OF_POCKET
    }
    
    private int ticksBeforeTeleport;
    private final EntityPlayer player;
    private final Pocket pocket;
    private final Direction direction;
    
    public TeleportPreparation(EntityPlayer player, int ticksBeforeTeleport, Pocket pocket, Direction direction) {
        this.player = player;
        this.ticksBeforeTeleport = ticksBeforeTeleport;
        this.pocket = pocket;
        this.direction = direction;
    }
    
    /**
     * Returns true, when the preparation is finished and the teleport has been done, so this can be cleaned up afterwards
     * @return
     */
    public boolean doPrepareTick() {
        if (ticksBeforeTeleport < 0)
            return true;
        
        ticksBeforeTeleport--;
        
        if (ticksBeforeTeleport <= 0) {
            if (direction == Direction.INTO_POCKET) {
                pocket.teleportTo(player);
            } else {
                pocket.teleportFrom(player);
            }
            return true;
        }
        
        
        return false;
    }
}
