package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigDouble;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryDouble extends ConfigEntry<ConfigDouble, Double> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
        String comment = processComment(annotation.comment());
        return getDouble(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue());
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
        String comment = processComment(annotation.comment());
        getDoubleProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue()).set(currentValue);
    }
}
