package net.gtn.dimensionalpocket;

import net.gtn.dimensionalpocket.client.ClientProxy;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.BiomeHelper;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.WorldProviderPocket;
import net.gtn.dimensionalpocket.common.core.config.ConfigHandler;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = "required-after:Forge@[10.12.1.1060,)")
public class DimensionalPockets {

    @Instance(Reference.MOD_ID)
    public static DimensionalPockets instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.dimensionalPocket);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DPLogger.init();

        ConfigHandler.init(event.getSuggestedConfigurationFile());

        ModBlocks.init();
        ModItems.init();

        ModItems.initRecipes();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.runServerSide();
        proxy.runClientSide();

        MinecraftForge.EVENT_BUS.register(proxy);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        DimensionManager.registerProviderType(Reference.DIMENSION_ID, WorldProviderPocket.class, false);
        DimensionManager.registerDimension(Reference.DIMENSION_ID, Reference.DIMENSION_ID);

        BiomeHelper.init();

        ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void onServerStarted(FMLServerStartingEvent event) {
        PocketRegistry.loadData();
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        PocketRegistry.saveData();
        ChunkLoaderHandler.ticketMap.clear();
    }
}
