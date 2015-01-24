package net.gtn.dimensionalpocket.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.gtn.dimensionalpocket.client.gui.GuiInfoBook;
import net.gtn.dimensionalpocket.client.gui.GuiPocketConfig;
import net.gtn.dimensionalpocket.client.renderer.item.ItemPocketRenderer;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocketWall;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.utils.MovingObjectPositionUtil;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    public static int currentPage = 0;

    @Override
    public void runClientSide() {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dimensionalPocket), new ItemPocketRenderer());
    	
        ClientRegistry.bindTileEntitySpecialRenderer(TileDimensionalPocket.class, new TileRendererPocket());
        ClientRegistry.bindTileEntitySpecialRenderer(TileDimensionalPocketWallConnector.class, new TileRendererPocketWall());
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
