package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigInteger;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryInteger extends ConfigEntry<ConfigInteger, Integer> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
        String comment = processComment(annotation.comment());
        return getInt(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment);
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
        String comment = processComment(annotation.comment());
        getIntProperty(annotation.category(), fieldName, defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
    }
}
