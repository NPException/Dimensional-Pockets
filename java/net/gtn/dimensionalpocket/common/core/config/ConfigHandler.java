package net.gtn.dimensionalpocket.common.core.config;

import java.io.File;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration configuration;

    public static void init(File configFile) {
        configuration = new Configuration(configFile);

        try {

            Reference.DIMENSION_ID = configuration.get("Gameplay", "Pocket Dimension ID", Reference.DIMENSION_ID_DEFAULT).getInt();

        } catch (Exception e) {
            DPLogger.severe(e);
        } finally {
            configuration.save();
        }
    }
}
