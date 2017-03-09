package net.gtn.dimensionalpocket.oc.api.configuration.discovery;

import com.google.common.base.Strings;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import net.gtn.dimensionalpocket.oc.api.configuration.Config.IConfigRegistrar;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.gtn.dimensionalpocket.oc.api.configuration.lib.IConfigRegistry;
import net.gtn.dimensionalpocket.oc.common.core.CoreProperties;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The config data manager for any given ModContainer.
 */
public class ConfigData {
    private static final File CONFIG_DIR = new File(".", "config");

    private Collection<String> ownedClasses;
    TreeMap<String, ConfigContainer> containerMap;
    private ModContainer modContainer;
    public final boolean isRegistrar;

    public ConfigData(ModContainer modContainer, Collection<String> ownedClasses) {
        this.modContainer = modContainer;
        copyOwnedClasses(ownedClasses);
        isRegistrar = modContainer.getMod() instanceof IConfigRegistrar;
        containerMap = new TreeMap<>(dataComparator);
    }

    private void copyOwnedClasses(Collection<String> ownedClasses) {
        ArrayList<String> sortedClasses = new ArrayList<>();
        for (String s : ownedClasses)
            if (!sortedClasses.contains(s))
                sortedClasses.add(s);
        this.ownedClasses = sortedClasses;
    }

    public void addRoot(ASMData asmData) {
        Map<String, Object> annotationInfo = asmData.getAnnotationInfo();

        String packageName = asmData.getClassName();

        int pkgIndex = packageName.lastIndexOf('.');
        if (pkgIndex > -1)
            packageName = packageName.substring(0, pkgIndex);
        packageName = packageName.replace(".", "/");

        if (!containerMap.containsKey(packageName)) {
            CoreProperties.logger.info("Discovered config controller inside: {}", packageName);

            File defaultConfig = getConfigForPackage(annotationInfo);

            CoreProperties.logger.info("Setting config: {}", defaultConfig);

            containerMap.put(packageName, new ConfigContainer(defaultConfig));
        } else {
            CoreProperties.logger.warn("Config controller discovered in the same root: {}. ", packageName);
            CoreProperties.logger.warn("THIS IS AN ERROR! Ignoring {}", asmData.getClassName());
        }
    }

    public void processIConfigRegistrar(IConfigRegistry registry) {
        CoreProperties.logger.info("Registering custom annotations for {}", modContainer.getModId());
        ((IConfigRegistrar) modContainer.getMod()).registerCustomAnnotations(registry);
    }

    public void processRoots() {
        for (String rootPackage : containerMap.keySet())
            containerMap.get(rootPackage).setChildClasses(getAllChildClasses(rootPackage));
    }

    public void processConfigContainers(ASMDataTable asmDataTable, Map<Class<? extends Annotation>, Class<? extends ConfigEntry<? extends Annotation, ?>>> annotationMap) {
        for (ConfigContainer configContainer : containerMap.values()) {
            configContainer.processAllClasses(asmDataTable, annotationMap);
            configContainer.operateOnConfig(false);
        }
    }

    public void save() {
        for (ConfigContainer configContainer : containerMap.values())
            configContainer.operateOnConfig(true);
    }

    private File getConfigForPackage(Map<String, Object> annotationInfo) {
        String configFile = (String) annotationInfo.get("configFile");
        if (Strings.isNullOrEmpty(configFile))
            return new File(CONFIG_DIR, modContainer.getModId() + ".cfg");
        if (!configFile.endsWith(".cfg"))
            configFile += ".cfg";
        return new File(CONFIG_DIR, configFile);
    }

    private Collection<String> getAllChildClasses(String rootPackage) {
        ArrayList<String> children = new ArrayList<>();
        Iterator<String> iterator = ownedClasses.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith(rootPackage)) {
                iterator.remove();
                children.add(s.replace("/", "."));
            }
        }
        return children;
    }

    /**
     * Go from largest to smallest, that way when the childClasses get processed it can pull them out of the pool, without them already belonging to a more generic package.
     */
    public static final Comparator<String> dataComparator = new Comparator<String>() {
        @Override
        public int compare(String data1, String data2) {
            return data2.length() - data1.length();
        }
    };
}
