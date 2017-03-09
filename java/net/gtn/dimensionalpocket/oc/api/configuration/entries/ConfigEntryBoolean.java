package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigBoolean;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryBoolean extends ConfigEntry<ConfigBoolean, Boolean> {

    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        return getBoolean(annotation.category(), fieldName, defaultValue, processComment(annotation.comment()));
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigBoolean annotation, Boolean currentValue, Boolean defaultValue) {
        getBooleanProperty(annotation.category(), fieldName, defaultValue, processComment(annotation.comment())).set(currentValue);
    }
}
