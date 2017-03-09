package net.gtn.dimensionalpocket.oc.api.configuration;

import com.google.common.base.Strings;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.gtn.dimensionalpocket.oc.api.configuration.discovery.ConfigData;
import net.gtn.dimensionalpocket.oc.api.configuration.entries.*;
import net.gtn.dimensionalpocket.oc.api.configuration.lib.IConfigRegistry;
import net.minecraftforge.common.MinecraftForge;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static net.gtn.dimensionalpocket.oc.api.configuration.Config.*;

public class ConfigHandler implements IConfigRegistry {

    private static ConfigHandler INSTANCE;

    // Once this is true, no more registering annotations.
    private static boolean init = false;
    private static boolean processed = false;
    private static boolean postProcessed = false;

    private static final Map<String, ConfigData> configMap = new LinkedHashMap<>();
    private static final Map<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap = new LinkedHashMap<>();

    static {
        annotationMap.put(ConfigBoolean.class, ConfigEntryBoolean.class);
        annotationMap.put(ConfigBooleanArray.class, ConfigEntryBooleanArray.class);
        annotationMap.put(ConfigInteger.class, ConfigEntryInteger.class);
        annotationMap.put(ConfigIntegerArray.class, ConfigEntryIntegerArray.class);
        annotationMap.put(ConfigFloat.class, ConfigEntryFloat.class);
        annotationMap.put(ConfigDouble.class, ConfigEntryDouble.class);
        annotationMap.put(ConfigDoubleArray.class, ConfigEntryDoubleArray.class);
        annotationMap.put(ConfigString.class, ConfigEntryBoolean.class);
        annotationMap.put(ConfigStringArray.class, ConfigEntryBoolean.class);
    }

    private ConfigHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void parseControllers(FMLPreInitializationEvent event) {
        if (init)
            return;
        init = true;

        ASMDataTable asmDataTable = event.getAsmData();
        Set<ASMData> asmDataSet = asmDataTable.getAll(Controller.class.getName());

        // Filters out all controller annotations and places them with their associated mod.
        for (ASMData data : asmDataSet) {
            ModCandidate candidate = data.getCandidate();
            for (ModContainer modContainer : candidate.getContainedMods()) {
                String modId = modContainer.getModId();
                if (!configMap.containsKey(modId))
                    configMap.put(modId, new ConfigData(modContainer, candidate.getClassList()));
                configMap.get(modId).addRoot(data);
            }
        }

        // Process all potential registrars first.
        for (ConfigData configValue : configMap.values())
            if (configValue.isRegistrar)
                configValue.processIConfigRegistrar(this);
        processed = true;

        for (ConfigData configData : configMap.values()) {
            // Organise all sub-packages.
            configData.processRoots();

            // Process all current classes associated with the ConfigContainer.
            configData.processConfigContainers(asmDataTable, annotationMap);
        }
        postProcessed = true;
    }

    /**
     * Only call this when you've had the chance from the interface.
     * To use this, implement {@link net.gtn.dimensionalpocket.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     * Please make sure you use that interface, that way I can guarantee that all the annotations are registered before all processing begins.
     */
    @Override
    public boolean registerAnnotation(final Class<? extends Annotation> clazz, final Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry) {
        if (processed || Modifier.isAbstract(configEntry.getModifiers()))
            return false;
        if (!annotationMap.containsKey(clazz))
            annotationMap.put(clazz, configEntry);
        return true;
    }

    public static void save(String modID) {
        if (!postProcessed || Strings.isNullOrEmpty(modID) || !Loader.isModLoaded(modID))
            return;
        if (configMap.containsKey(modID))
            configMap.get(modID).save();
    }

    @Override
    public String toString() {
        return annotationMap.toString();
    }

    public static void initConfigHandler(FMLPreInitializationEvent event) {
        if (INSTANCE != null)
            return;

        INSTANCE = new ConfigHandler();
        INSTANCE.parseControllers(event);
    }
}
