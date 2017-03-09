package net.gtn.dimensionalpocket.oc.api.configuration;

import static net.minecraftforge.common.config.Property.Type.STRING;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.oc.common.utils.Localise;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class ConfigEntry<T extends Annotation, D> {

    private Map<Field, FieldWrapper> configMap;
    protected Configuration config;

    public ConfigEntry() {
        configMap = new LinkedHashMap<>();
    }

    public ConfigEntry<T, D> setConfig(Configuration config) {
        this.config = config;
        return this;
    }

    public void add(Field field, T annotation) {
        if (!configMap.containsKey(field))
            configMap.put(field, new FieldWrapper(field, annotation));
    }

    public void processFields(boolean saveFlag) {
        for (Map.Entry<Field, FieldWrapper> entry : configMap.entrySet()) {
            Field field = entry.getKey();
            FieldWrapper wrapper = entry.getValue();

            String fieldName = field.getName().toLowerCase();
            try {
                if (!saveFlag) {
                    Object object = loadAnnotation(config, fieldName, wrapper.annotation, wrapper.currentValue(), wrapper.defaultValue);
                    field.set(null, object);
                } else
                    saveAnnotation(config, fieldName, wrapper.annotation, wrapper.currentValue(), wrapper.defaultValue);
            } catch (Exception e) {
                DPLogger.severe(String.format("Failed to configure field: %s, with annotated type: %s", fieldName, wrapper.annotation.annotationType().getSimpleName()), e);
            }
        }
    }

    /**
     * For your use.
     * It processes all strings, attempts to localise each one, and puts in all in one string.
     *
     * @param comments - The comments in question.
     * @return - The coherent "fully localised"* string. *Unless it fails... :/
     */
    public String processComment(String... comments) {
        if (comments.length == 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String comment : comments) {
            stringBuilder.append(Localise.safeTranslate(comment));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString().trim() + System.lineSeparator();
    }

    /**
     * @param config       -   The config instance of the file that the system determined was the correct hierarchy
     * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the configuration file.
     * @param annotation   -   The annotation type that was applied to the field.
     * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
     * @param defaultValue -   The default value, if any, already assigned to the field.
     * @return -   The object to set the field to. Can be null if no defaultValue is given.
     */
    public abstract Object loadAnnotation(Configuration config, String fieldName, T annotation, D currentValue, D defaultValue);

    /**
     * @param config       -   The config instance of the file that the system determined was the correct hierarchy
     * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the configuration file.
     * @param annotation   -   The annotation type that was applied to the field.
     * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
     * @param defaultValue -   The default value, if any, already assigned to the field.
     */
    public abstract void saveAnnotation(Configuration config, String fieldName, T annotation, D currentValue, D defaultValue);

    private class FieldWrapper {
        public D defaultValue;
        public T annotation;
        private Field field;

        @SuppressWarnings("unchecked")
        public FieldWrapper(Field field, T annotation) {
            this.field = field;
            this.annotation = annotation;
            try {
                this.defaultValue = (D) field.get(null);
            } catch (IllegalAccessException ignored) {
                this.defaultValue = null;
            }
        }

        @SuppressWarnings("unchecked")
        private D currentValue() {
            try {
                return (D) field.get(null);
            } catch (IllegalAccessException e) {
                DPLogger.info("Failed to get current value.");
                Throwables.propagate(e);
            }
            return null;
        }
    }

    /**
     * Getters for properties and values.
     * This way you don't have to deal with the inconsistencies of Forge's config methods.
     */

    public boolean getBoolean(String category, String key, boolean defaultValue) {
        return getBooleanProperty(category, key, defaultValue, null, key).getBoolean();
    }

    public Property getBooleanProperty(String category, String key, boolean defaultValue) {
        return getBooleanProperty(category, key, defaultValue, null, key);
    }

    public boolean getBoolean(String category, String key, boolean defaultValue, String comment) {
        return getBooleanProperty(category, key, defaultValue, comment, key).getBoolean();
    }

    public Property getBooleanProperty(String category, String key, boolean defaultValue, String comment) {
        return getBooleanProperty(category, key, defaultValue, comment, key);
    }

    public boolean getBoolean(String category, String key, boolean defaultValue, String comment, String langKey) {
        return getBooleanProperty(category, key, defaultValue, comment, langKey).getBoolean();
    }

    public Property getBooleanProperty(String category, String key, boolean defaultValue, String comment, String langKey) {
        Property prop = config.get(category, key, defaultValue, comment);
        prop.setLanguageKey(langKey);
        prop.comment += " [default: " + defaultValue + "]";
        return prop;
    }

    public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues) {
        return getBooleanArrayProperty(category, key, defaultValues, null, false, -1).getBooleanList();
    }

    public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues) {
        return getBooleanArrayProperty(category, key, defaultValues, null, false, -1);
    }

    public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues, String comment) {
        return getBooleanArrayProperty(category, key, defaultValues, comment, false, -1).getBooleanList();
    }

    public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues, String comment) {
        return getBooleanArrayProperty(category, key, defaultValues, comment, false, -1);
    }

    public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength) {
        return getBooleanArrayProperty(category, key, defaultValues, comment, isListLengthFixed, maxListLength).getBooleanList();
    }

    public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength) {
        return config.get(category, key, defaultValues, comment, isListLengthFixed, maxListLength);
    }

    public double getDouble(String category, String key, double defaultValue) {
        return getDoubleProperty(category, key, defaultValue, (String) null, Double.MIN_VALUE, Double.MAX_VALUE).getDouble();
    }

    public Property getDoubleProperty(String category, String key, double defaultValue) {
        return getDoubleProperty(category, key, defaultValue, (String) null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public double getDouble(String category, String key, double defaultValue, String comment) {
        return getDoubleProperty(category, key, defaultValue, comment, Double.MIN_VALUE, Double.MAX_VALUE).getDouble();
    }

    public Property getDoubleProperty(String category, String key, double defaultValue, String comment) {
        return getDoubleProperty(category, key, defaultValue, comment, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public double getDouble(String category, String key, double defaultValue, String comment, double minValue, double maxValue) {
        return getDoubleProperty(category, key, defaultValue, comment, minValue, maxValue).getDouble();
    }

    public Property getDoubleProperty(String category, String key, double defaultValue, String comment, double minValue, double maxValue) {
        return config.get(category, key, defaultValue, comment, minValue, maxValue);
    }

    public double[] getDoubleArray(String category, String key, double[] defaultValues) {
        return getDoubleArrayProperty(category, key, defaultValues, null, Double.MIN_VALUE, Double.MAX_VALUE, false, -1).getDoubleList();
    }

    public Property getDoubleArrayProperty(String category, String key, double[] defaultValues) {
        return getDoubleArrayProperty(category, key, defaultValues, null, Double.MIN_VALUE, Double.MAX_VALUE, false, -1);
    }

    public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment) {
        return getDoubleArrayProperty(category, key, defaultValues, comment, Double.MIN_VALUE, Double.MAX_VALUE, false, -1).getDoubleList();
    }

    public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment) {
        return getDoubleArrayProperty(category, key, defaultValues, comment, Double.MIN_VALUE, Double.MAX_VALUE, false, -1);
    }

    public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue) {
        return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, false, -1).getDoubleList();
    }

    public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue) {
        return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, false, -1);
    }

    public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, boolean isListLengthFixed, int maxListLength) {
        return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, isListLengthFixed, maxListLength).getDoubleList();
    }

    public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, boolean isListLengthFixed, int maxListLength) {
        return config.get(category, key, defaultValues, comment, minValue, maxValue, isListLengthFixed, maxListLength);
    }

    public float getFloat(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
        return getFloat(category, key, defaultValue, minValue, maxValue, comment, key);
    }

    public Property getFloatProperty(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
        return getFloatProperty(category, key, defaultValue, minValue, maxValue, comment, key);
    }


    public float getFloat(String category, String key, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
        Property prop = getFloatProperty(category, key, defaultValue, minValue, maxValue, comment, langKey);
        try {
            return Float.parseFloat(prop.getString()) < minValue ? minValue : (Float.parseFloat(prop.getString()) > maxValue ? maxValue : Float.parseFloat(prop.getString()));
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    public Property getFloatProperty(String category, String key, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
        Property prop = config.get(category, key, Float.toString(defaultValue), key);
        prop.setLanguageKey(langKey);
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop;
    }

    public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
        return getInt(category, key, defaultValue, minValue, maxValue, comment, key);
    }

    public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
        return getIntProperty(category, key, defaultValue, minValue, maxValue, comment, key);
    }

    public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
        Property prop = getIntProperty(category, key, defaultValue, minValue, maxValue, comment, langKey);
        return prop.getInt(defaultValue) < minValue ? minValue : (prop.getInt(defaultValue) > maxValue ? maxValue : prop.getInt(defaultValue));
    }

    public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
        Property prop = config.get(category, key, defaultValue);
        prop.setLanguageKey(langKey);
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop;
    }

    public int[] getIntArray(String category, String key, int[] defaultValues) {
        return getIntArrayProperty(category, key, defaultValues, (String) null, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1).getIntList();
    }

    public Property getIntArrayProperty(String category, String key, int[] defaultValues) {
        return getIntArrayProperty(category, key, defaultValues, (String) null, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1);
    }

    public int[] getIntArray(String category, String key, int[] defaultValues, String comment) {
        return getIntArrayProperty(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1).getIntList();
    }

    public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment) {
        return getIntArrayProperty(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1);
    }

    public int[] getIntArray(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue) {
        return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, false, -1).getIntList();
    }

    public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue) {
        return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, false, -1);
    }

    public int[] getIntArray(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, boolean isListLengthFixed, int maxListLength) {
        return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, isListLengthFixed, maxListLength).getIntList();
    }

    public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, boolean isListLengthFixed, int maxListLength) {
        return config.get(category, key, defaultValues, comment, minValue, maxValue, isListLengthFixed, maxListLength);
    }

    public String getString(String category, String key, String defaultValue) {
        return getStringProperty(category, key, defaultValue, null, (String[]) null).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue) {
        return getStringProperty(category, key, defaultValue, null, (String[]) null);
    }

    public String getString(String category, String key, String defaultValue, String comment) {
        return getStringProperty(category, key, defaultValue, comment, (String[]) null).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue, String comment) {
        return getStringProperty(category, key, defaultValue, comment, (String[]) null);
    }

    public String getString(String category, String key, String defaultValue, String comment, String[] validValues) {
        return getStringProperty(category, key, defaultValue, comment, validValues).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue, String comment, String[] validValues) {
        return getStringProperty(category, key, defaultValue, comment, key, validValues);
    }

    public String getString(String category, String key, String defaultValue, String comment, String langKey, String[] validValues) {
        return getStringProperty(category, key, defaultValue, comment, langKey, validValues).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue, String comment, String langKey, String[] validValues) {
        Property prop = config.get(category, key, defaultValue, comment, STRING);
        prop.setLanguageKey(langKey);
        if (validValues != null)
            prop.setValidValues(validValues);
        return prop;
    }

    public String getString(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
        return getStringProperty(category, key, defaultValue, comment, key, validationPattern).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
        return getStringProperty(category, key, defaultValue, comment, key, validationPattern);
    }

    public String getString(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
        return getStringProperty(category, key, defaultValue, comment, langKey, validationPattern).getString();
    }

    public Property getStringProperty(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
        Property prop = config.get(category, key, defaultValue, comment, STRING);
        prop.setLanguageKey(langKey);
        if (validationPattern != null)
            prop.setValidationPattern(validationPattern);
        return prop;
    }

    public String[] getStringArray(String category, String key, String[] defaultValues) {
        return getStringArrayProperty(category, key, defaultValues, null, false, -1, null).getStringList();
    }

    public Property getStringArrayProperty(String category, String key, String[] defaultValues) {
        return getStringArrayProperty(category, key, defaultValues, null, false, -1, null);
    }

    public String[] getStringArray(String category, String key, String[] defaultValues, String comment) {
        return getStringArrayProperty(category, key, defaultValues, comment, false, -1, null).getStringList();
    }

    public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment) {
        return getStringArrayProperty(category, key, defaultValues, comment, false, -1, null);
    }

    public String[] getStringArray(String category, String key, String[] defaultValues, String comment, Pattern validationPattern) {
        return getStringArrayProperty(category, key, defaultValues, comment, false, -1, validationPattern).getStringList();
    }

    public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment, Pattern validationPattern) {
        return getStringArrayProperty(category, key, defaultValues, comment, false, -1, validationPattern);
    }

    public String[] getStringArray(String category, String key, String[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength, Pattern validationPattern) {
        return getStringArrayProperty(category, key, defaultValues, comment, isListLengthFixed, maxListLength, validationPattern).getStringList();
    }

    public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength, Pattern validationPattern) {
        return config.get(category, key, defaultValues, comment, isListLengthFixed, maxListLength, validationPattern);
    }
}
