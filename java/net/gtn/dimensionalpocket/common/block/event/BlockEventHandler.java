package net.gtn.dimensionalpocket.common.block.event;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {
    
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!Reference.CAN_BREAK_POCKET_WALL_IN_CREATIVE && event.block == ModBlocks.dimensionalPocketWall) {
            event.setCanceled(true);
        }
    }
}
