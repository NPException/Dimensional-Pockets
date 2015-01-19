package net.gtn.dimensionalpocket.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.gtn.dimensionalpocket.client.gui.GuiInfoBook;
import net.gtn.dimensionalpocket.client.gui.GuiPocketConfig;
import net.gtn.dimensionalpocket.client.renderer.BlockPocketRenderer;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.core.utils.MovingObjectPositionUtil;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    public static int currentPage = 0;

    @Override
    public void runClientSide() {
        RenderingRegistry.registerBlockHandler(new BlockPocketRenderer());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiInfoBook(player);
            case 1:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileDimensionalPocket) {
                    int side = MovingObjectPositionUtil.getCurrentMousedOverSide(player);
                    int looking = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
                    return new GuiPocketConfig(player, (TileDimensionalPocket) tileEntity, side, looking);
                }
                break;
            default:
                return null;
        }
        return null;
    }

}
