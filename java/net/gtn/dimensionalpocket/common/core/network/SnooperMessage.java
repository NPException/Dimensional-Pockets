package net.gtn.dimensionalpocket.common.core.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.gtn.dimensionalpocket.common.core.utils.Snooper;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


/**
 * This message get's sent to the server on login if the client has the snooper
 * settings enabled or not.
 *
 * @author NPException
 *
 */
public class SnooperMessage implements IMessage {
	private boolean canSnoop;

	public SnooperMessage() {
	}

	public SnooperMessage(boolean canSnoop) {
		this.canSnoop = canSnoop;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		canSnoop = 0 != ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarShort(buf, canSnoop ? 1 : 0);
	}

	public static class Handler implements IMessageHandler<SnooperMessage, IMessage> {
		@Override
		public IMessage onMessage(SnooperMessage message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			UUID uuid = player.getGameProfile().getId();
			if (uuid != null) {
				Snooper.playerAdjustSnoop(uuid, message.canSnoop);
				System.out.println(String.format("Received canSnoop=%s from %s", String.valueOf(message.canSnoop), player.getDisplayName()));
			}
			return null;
		}
	}
}
