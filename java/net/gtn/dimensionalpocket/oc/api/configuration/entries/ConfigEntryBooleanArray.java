package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigBooleanArray;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryBooleanArray extends ConfigEntry<ConfigBooleanArray, boolean[]> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
        String comment = processComment(annotation.comment());
        return getBooleanArray(annotation.category(), fieldName, defaultValue, comment, annotation.isListLengthFixed(), annotation.maxListLength());
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
        String comment = processComment(annotation.comment());
        getBooleanArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.isListLengthFixed(), annotation.maxListLength()).set(currentValue);
    }
}
