package net.gtn.dimensionalpocket.oc.common.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;

/**
 * Just a fancy name for SimpleNetworkWrapperWrapper.
 * Yes, this entire class is basically just to make registration easy.
 * That's it.
 * Nothing really special.
 * <p/>
 * Note: These messages aren't designed for massive loads.
 * They're for small messages.
 */
public class NetworkDispatcher {

    private SimpleNetworkWrapper network;
    private int discriminator = 0;

    public NetworkDispatcher(String channelName) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }

    /**
     * @param clazz  Class with all the message goodness, must extend MessageAbstract.
     * @param target Side that the message is being sent to.
     */
    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends MessageAbstract<REQ, REPLY>> clazz, Side target) {
        network.registerMessage((Class<? extends IMessageHandler<REQ, REPLY>>) clazz, (Class<REQ>) clazz, discriminator++, target);
    }

    /**
     * Pass through.
     */
    public Packet getPacketFrom(IMessage message) {
        return network.getPacketFrom(message);
    }

    /**
     * Pass through.
     */
    public void sendToAll(IMessage message) {
        network.sendToAll(message);
    }

    /**
     * Pass through.
     */
    public void sendTo(IMessage message, EntityPlayerMP player) {
        network.sendTo(message, player);
    }

    /**
     * Pass through.
     */
    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        network.sendToAllAround(message, point);
    }

    /**
     * Pass through.
     */
    public void sendToDimension(IMessage message, int dimensionId) {
        network.sendToDimension(message, dimensionId);
    }

    /**
     * Pass through.
     */
    public void sendToServer(IMessage message) {
        network.sendToServer(message);
    }

    /**
     * Unified message class.
     * Makes the registration about 20 chars cleaner. :/
     * <p/>
     * Note: Because of the implementation, the instance called for creation of the packet is not the same instance used for processing it.
     */
    public static abstract class MessageAbstract<REQ extends IMessage, REPLY extends IMessage> implements IMessage, IMessageHandler<REQ, REPLY> {
    }

}
