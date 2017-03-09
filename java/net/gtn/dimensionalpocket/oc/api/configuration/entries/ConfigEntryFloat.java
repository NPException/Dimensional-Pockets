package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigFloat;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryFloat extends ConfigEntry<ConfigFloat, Float> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
        String comment = processComment(annotation.comment());
        return getFloat(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
        String comment = processComment(annotation.comment());
        getFloatProperty(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
    }
}
