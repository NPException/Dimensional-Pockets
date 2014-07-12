package net.gtn.dimensionalpocket.client;

import net.gtn.dimensionalpocket.client.gui.GuiConfig;
import net.gtn.dimensionalpocket.client.gui.GuiInfoBook;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.core.utils.MovingObjectPositionUtil;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    public static int currentPage = 0;

    @Override
    public void runClientSide() {

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiInfoBook();
            case 1:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileDimensionalPocket) {
                    int side = MovingObjectPositionUtil.getCurrentMousedOverSide(player);
                    return new GuiConfig((TileDimensionalPocket) tileEntity, side);
                }
                break;
            default:
                return null;
        }
        return null;
    }

}
