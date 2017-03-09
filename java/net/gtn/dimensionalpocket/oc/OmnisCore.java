package net.gtn.dimensionalpocket.oc;

import static net.gtn.dimensionalpocket.oc.common.core.CoreProperties.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.gtn.dimensionalpocket.oc.api.collect.Graph;
import net.gtn.dimensionalpocket.oc.api.configuration.Config;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigHandler;
import net.gtn.dimensionalpocket.oc.api.network.NetworkCore;
import net.gtn.dimensionalpocket.oc.api.network.NetworkInstance;
import net.gtn.dimensionalpocket.oc.api.network.search.SearchThread;
import net.gtn.dimensionalpocket.oc.common.CommonProxy;
import net.gtn.dimensionalpocket.oc.common.core.DebugHelper;
import net.gtn.dimensionalpocket.oc.common.core.network.MessageGuiNotify;
import net.gtn.dimensionalpocket.oc.common.core.network.NetworkDispatcher;

@Config.Controller()
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class OmnisCore {

    @Instance(MOD_ID)
    public static OmnisCore instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    public static NetworkDispatcher networkDispatcher;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("-- Pre-Initialising " + MOD_ID + " (" + VERSION + ") --");

        ConfigHandler.initConfigHandler(event);
        DebugHelper.checkSysOverrides();

        logger.info("Setting up internal network - Channel ID: " + MOD_ID);
        networkDispatcher = new NetworkDispatcher(MOD_ID);
        networkDispatcher.registerMessage(MessageGuiNotify.class, Side.SERVER);
        logger.info("Success! Network fully integrated.");
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("-- Initialising --");

        logger.info("Starting OmnisCore|NST");
        SearchThread.getInstance().start();

        logger.info("Preloading Network|API");
        new NetworkInstance();
        new NetworkCore();
        new Graph<>();
        logger.info("Finished Preloading Network|API");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("-- Post-Initialising --");
    }
}
