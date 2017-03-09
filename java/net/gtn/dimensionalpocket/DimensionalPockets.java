package net.gtn.dimensionalpocket;

import static net.gtn.dimensionalpocket.common.core.utils.DPAnalytics.analytics;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.gtn.dimensionalpocket.common.CommonProxy;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.BiomeHelper;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.WorldProviderPocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPAnalytics;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.oc.api.configuration.Config.Controller;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigHandler;
import net.gtn.dimensionalpocket.oc.client.CreativeTabSimple;
import net.gtn.dimensionalpocket.oc.common.core.network.MessageGuiNotify;
import net.gtn.dimensionalpocket.oc.common.core.network.NetworkDispatcher;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;


@Controller(configFile = "DimensionalPockets")
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.2.1230,);after:TConstruct;after:Thaumcraft;")
public class DimensionalPockets {

	@Instance(Reference.MOD_ID)
	public static DimensionalPockets instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static CreativeTabSimple creativeTab = new CreativeTabSimple(Reference.MOD_ID);
	
	public static NetworkDispatcher networkDispatcher;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		DPLogger.init(LogManager.getLogger(Reference.MOD_NAME.replaceAll(" ", "")));

		analytics = new DPAnalytics();
		analytics.initShutdownHook();
		
		ConfigHandler.initConfigHandler(event);

      networkDispatcher = new NetworkDispatcher(Reference.MOD_ID);
      networkDispatcher.registerMessage(MessageGuiNotify.class, Side.SERVER);

		proxy.preInitServerSide();
		proxy.preInitClientSide();

		ModBlocks.init();
		ModItems.init();

		creativeTab.setIcon(ModBlocks.dimensionalPocket);

		ModItems.initRecipes();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initServerSide();
		proxy.initClientSide();

		MinecraftForge.EVENT_BUS.register(proxy);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		DimensionManager.registerProviderType(Reference.DIMENSION_ID, WorldProviderPocket.class, true);
		DimensionManager.registerDimension(Reference.DIMENSION_ID, Reference.DIMENSION_ID);

		BiomeHelper.init();

		if (Reference.KEEP_POCKET_ROOMS_CHUNK_LOADED) {
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderHandler());
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInitServerSide();
		proxy.postInitClientSide();

		analytics.eventDesign("ModLoaded:" + (analytics.userPrefix()), Integer.valueOf(1));
	}

	@EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		// PocketRegistry needs to be loaded before the TileEntities are read from NBT
		PocketRegistry.loadData();
	}

	@EventHandler
	public void onServerStarted(FMLServerStartingEvent event) {
		PocketRegistry.initChunkLoading();
		PocketRegistry.validatePocketConnectors();
	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
		PocketRegistry.saveData();
		ChunkLoaderHandler.clearTicketMap();
	}
}
