package net.gtn.dimensionalpocket.client.tickhandler;

import java.lang.reflect.Method;

import net.gtn.dimensionalpocket.client.utils.version.VersionChecker;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientPlayerTickEventHandler {
    
    private static boolean checkForVersion = Reference.DO_VERSION_CHECK;
    
    public static boolean hideStuffFromNEI = false;
    
    /**
     * Hides blocks and items from NEI that it should not show. The Dimensional Pocket Wall f.e.
     */
    private static void hideStuffFromNEI() {
        try {
            Class<?> neiApiClass = Class.forName("codechicken.nei.api.API");
            Method hideItemMethod = neiApiClass.getDeclaredMethod("hideItem", ItemStack.class);
            hideItemMethod.invoke(null, new ItemStack(ModBlocks.dimensionalPocketWall));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void event(PlayerTickEvent evt) {
        if (hideStuffFromNEI) {
            hideStuffFromNEI = false;
            hideStuffFromNEI();
        }
        
        if (checkForVersion) {
            checkForVersion = false;
            VersionChecker.checkUpToDate(evt.player);
        }
    }
}
