package net.gtn.dimensionalpocket.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void runClientSide() {

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                break;
        }
        return null;
    }

}
