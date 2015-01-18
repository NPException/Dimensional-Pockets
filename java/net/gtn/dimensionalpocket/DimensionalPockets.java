package net.gtn.dimensionalpocket;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import me.jezza.oc.api.configuration.Config;
import me.jezza.oc.client.CreativeTabSimple;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.BiomeHelper;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.WorldProviderPocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;

@Config.Controller(configFile = "DimensionalPockets")
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.12.1.1060,);after:TConstruct")
public class DimensionalPockets {

    @Instance(Reference.MOD_ID)
    public static DimensionalPockets instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabSimple creativeTab = new CreativeTabSimple(Reference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DPLogger.init(event.getModLog());

        ModBlocks.init();
        ModItems.init();

        creativeTab.setIcon(ModBlocks.dimensionalPocket);

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
