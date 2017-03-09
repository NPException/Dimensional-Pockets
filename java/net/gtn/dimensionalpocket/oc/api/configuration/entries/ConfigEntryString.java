package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigString;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryString extends ConfigEntry<ConfigString, String> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        return getString(annotation.category(), fieldName, defaultValue, comment, annotation.validValues());
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
        String comment = processComment(annotation.comment());
        getStringProperty(annotation.category(), fieldName, defaultValue, comment, annotation.validValues()).set(currentValue);
    }
}
