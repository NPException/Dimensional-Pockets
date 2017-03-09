package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigIntegerArray;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryIntegerArray extends ConfigEntry<ConfigIntegerArray, int[]> {
    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
        String comment = processComment(annotation.comment());
        return getIntArray(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength());
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
        String comment = processComment(annotation.comment());
        getIntArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).set(currentValue);
    }
}
