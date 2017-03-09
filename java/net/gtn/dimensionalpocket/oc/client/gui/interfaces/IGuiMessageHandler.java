package net.gtn.dimensionalpocket.oc.client.gui.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * As weird as the naming goes, this is used on the server to process a client-side click if you want something updated in real-time.
 */
public interface IGuiMessageHandler {

    public void onClientClick(EntityPlayer player, int id, int process);

}
