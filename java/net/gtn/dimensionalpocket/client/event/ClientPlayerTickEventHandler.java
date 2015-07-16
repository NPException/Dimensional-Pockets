package net.gtn.dimensionalpocket.client.event;

import java.lang.reflect.Method;

import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.gtn.dimensionalpocket.client.utils.version.VersionChecker;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.network.DPNetwork;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientPlayerTickEventHandler {

    public static boolean hideStuffFromNEI = false;

    private static boolean checkForVersion = Reference.DO_VERSION_CHECK;

    private static boolean sendSnooper = true;

    /**
     * Hides blocks and items from NEI that it should not show. The Dimensional
     * Pocket Wall f.e.
     */
    private static void hideStuffFromNEI() {
        try {
            Class<?> neiApiClass = Class.forName("codechicken.nei.api.API");
            Method hideItemMethod = neiApiClass.getDeclaredMethod("hideItem", ItemStack.class);
            hideItemMethod.invoke(null, new ItemStack(ModBlocks.dimensionalPocketWall));
        } catch (Exception e) {
            DPLogger.warning("could not finish method \"hideStuffFromNEI()\"");
        }
    }

    private static void checkPlayerForNetherCrystal(EntityPlayer player) {
        ItemStack equippedItem = player.getCurrentEquippedItem();
        TileRendererPocket.doIndicateSides = equippedItem != null && equippedItem.getItem() == ModItems.netherCrystal;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void event(PlayerTickEvent evt) {
        if (evt.player != Minecraft.getMinecraft().thePlayer)
            return;

        if (hideStuffFromNEI) {
            hideStuffFromNEI = false;
            hideStuffFromNEI();
        }

        if (checkForVersion) {
            checkForVersion = false;
            VersionChecker.checkUpToDate(evt.player);
        }

        if (sendSnooper) {
            sendSnooper = false;
            DPNetwork.sendSnooperSetting();
        }

        // check for Nether Crystal in hand to trigger side color coding of pockets
        checkPlayerForNetherCrystal(evt.player);
    }
}
