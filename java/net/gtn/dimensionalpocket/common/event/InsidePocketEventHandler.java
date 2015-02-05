package net.gtn.dimensionalpocket.common.event;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class InsidePocketEventHandler {
    
    @SubscribeEvent
    public void onEnderTeleport(EnderTeleportEvent event) {
        // stop enderpearls from working
        if (event.entityLiving.worldObj.provider.dimensionId == Reference.DIMENSION_ID) {
            event.setCanceled(true);
        }
    }
}
