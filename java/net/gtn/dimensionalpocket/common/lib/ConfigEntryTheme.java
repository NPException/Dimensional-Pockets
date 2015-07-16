package net.gtn.dimensionalpocket.common.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.jezza.oc.api.configuration.ConfigEntry;
import net.gtn.dimensionalpocket.client.theme.Theme;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


public class ConfigEntryTheme extends ConfigEntry<ConfigEntryTheme.ConfigTheme, Theme> {

	public ConfigEntryTheme() {
	}

	//    @Override
	//    public Object loadAnnotation(Configuration config, String fieldName, ConfigTheme annotation, Theme currentValue, Theme defaultValue) {
	//        String string = config.getString(fieldName, annotation.category(), defaultValue.name(), processComment(annotation.comment()));
	//        return Theme.valueOf(string);
	//    }
	//
	//    @Override
	//    public void saveAnnotation(Configuration config, String fieldName, ConfigTheme annotation, Theme currentValue, Theme defaultValue) {
	//        String comment = processComment(annotation.comment());
	//        Property prop = config.get(annotation.category(), fieldName, defaultValue.name());
	//        prop.setLanguageKey(fieldName);
	//        prop.comment = comment + " [default: " + defaultValue + "]";
	//        prop.set(currentValue.name());
	//    }

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface ConfigTheme {
		String category();

		String[] comment() default {};
	}

	@Override
	public Object processAnnotation(Configuration config, String fieldName, ConfigTheme annotation, Theme defaultValue) {
		String comment = processComment(annotation.comment());
		Property prop = config.get(annotation.category(), fieldName, defaultValue.name());
		String current = prop.getString();
		prop.setLanguageKey(fieldName);
		prop.comment = comment + " [default: " + defaultValue + "]";
		prop.set(current);
		return Theme.valueOf(current);
	}
}
