package net.gtn.dimensionalpocket.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.tileentity.TileDPFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    @Override
    public void runClientSide() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileDPFrame.class, new RenderFrame());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            
        }
        return null;
    }

}
