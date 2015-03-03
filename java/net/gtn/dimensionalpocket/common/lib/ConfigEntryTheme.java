package net.gtn.dimensionalpocket.common.lib;

import me.jezza.oc.api.configuration.ConfigEntry;
import net.gtn.dimensionalpocket.client.theme.Theme;
import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigEntryTheme extends ConfigEntry<ConfigEntryTheme.ConfigTheme, Theme> {


    public ConfigEntryTheme() {
    }

    @Override
    public Object processAnnotation(Configuration config, String fieldName, ConfigTheme annotation, Theme defaultValue) {
        String string = config.getString(fieldName, annotation.category(), defaultValue.name(), processComment(annotation.comment()));
        return Theme.valueOf(string);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigTheme {
        String category();

        String[] comment() default {};
    }
}
