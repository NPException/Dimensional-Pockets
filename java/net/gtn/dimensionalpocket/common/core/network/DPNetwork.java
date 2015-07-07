package net.gtn.dimensionalpocket.common.core.network;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class DPNetwork {
    private DPNetwork() {
    } // singleton. nothing to see here.

    private static boolean init = true;

    private static SimpleNetworkWrapper network;

    public static void init() {
        if (!init)
            return;
        init = false;

        network = NetworkRegistry.INSTANCE.newSimpleChannel("DimensionalPockets_Channel");
        network.registerMessage(SnooperMessage.Handler.class, SnooperMessage.class, 0, Side.SERVER);
    }

    /**
     * Sends the snooper setting to the server
     */
    public static void sendSnooperSetting() {
        network.sendToServer(new SnooperMessage(Minecraft.getMinecraft().gameSettings.snooperEnabled));
    }
}
