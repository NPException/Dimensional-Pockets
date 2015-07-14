package net.gtn.dimensionalpocket.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.gtn.dimensionalpocket.client.commands.RenderTweakCommand;
import net.gtn.dimensionalpocket.client.event.ClientEventHandler;
import net.gtn.dimensionalpocket.client.event.ClientPlayerTickEventHandler;
import net.gtn.dimensionalpocket.client.gui.GuiInfoBook;
import net.gtn.dimensionalpocket.client.renderer.item.ItemPocketRenderer;
import net.gtn.dimensionalpocket.client.renderer.shader.ShaderHelper;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocketWall;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.event.BlockEventHandler;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    public static int currentPage = 0;

    @Override
    public void initClientSide() {
        ShaderHelper.initShaders();
        
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dimensionalPocket), new ItemPocketRenderer());
    	
        ClientRegistry.bindTileEntitySpecialRenderer(TileDimensionalPocket.class, new TileRendererPocket());
        ClientRegistry.bindTileEntitySpecialRenderer(TileDimensionalPocketWallConnector.class, new TileRendererPocketWall());
        
        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());

        ClientEventHandler ceh = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(ceh);
        FMLCommonHandler.instance().bus().register(ceh);
    }
    
    @Override
    public void postInitClientSide() {
        FMLCommonHandler.instance().bus().register(new ClientPlayerTickEventHandler());
        ClientCommandHandler.instance.registerCommand(new RenderTweakCommand());

        ClientPlayerTickEventHandler.hideStuffFromNEI = Loader.isModLoaded("NotEnoughItems");
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiInfoBook(player);
            default:
                return null;
        }
    }

}
