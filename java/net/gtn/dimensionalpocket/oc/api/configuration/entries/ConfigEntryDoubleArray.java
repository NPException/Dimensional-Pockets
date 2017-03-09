package net.gtn.dimensionalpocket.oc.api.configuration.entries;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigDoubleArray;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public class ConfigEntryDoubleArray extends ConfigEntry<ConfigDoubleArray, double[]> {

    @Override
    public Object loadAnnotation(Configuration config, String fieldName, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
        String comment = processComment(annotation.comment());
        return getDoubleArray(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength());
    }

    @Override
    public void saveAnnotation(Configuration config, String fieldName, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
        String comment = processComment(annotation.comment());
        getDoubleArrayProperty(annotation.category(), fieldName, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.isListLengthFixed(), annotation.maxListLength()).set(currentValue);
    }
}
