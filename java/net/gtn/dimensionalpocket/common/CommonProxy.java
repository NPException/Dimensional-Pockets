package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.lib.Strings;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

    public void runServerSide() {
        registerTileEntities();
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileDimensionalPocket.class, Strings.BLOCK_POCKET);
    }

    public void runClientSide() {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

}
