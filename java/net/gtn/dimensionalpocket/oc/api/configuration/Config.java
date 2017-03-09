package net.gtn.dimensionalpocket.oc.api.configuration;

import net.gtn.dimensionalpocket.oc.api.configuration.lib.IConfigRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Config {

    /**
     * If you have a custom config file location, override configFile() with the location.
     * NOTE: That ANY string that configFile returns WILL be pushed on the end of the default config directory.
     * EG, configFile() return "AwesomeMod/awesomeMod.cfg";
     * ConfigHandler will try locating the config file as such "config/AwesomeMod/awesomeMod.cfg"
     * If it fails, it will generate it.
     * <p/>
     * saveOnExit(); Whether or not OmnisCore will save the config for you upon server exit.
     * <p/>
     * SAVE_ON_MANUAL_TRIGGER is the only category that can, surprise!, be saved by a manual trigger.
     * SAVE_ON_WORLD_CLOSE will save on the world being closed.
     * SAVE_ON_GAME_CLOSE will save on the game being closed.
     * <p/>
     * Each category is triggered by the ones below it.
     * For example, I fire a manual save.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Controller {
        String configFile() default "";
    }

    /**
     * If the main mod class has implemented this, then the method will be fired before ANY config annotations are read on any mod.
     * This way you have the ability to add any custom annotations.
     * NOTE: THIS MUST BE IMPLEMENTED ON THE MAIN MOD CLASS TO TAKE EFFECT
     */
    public static interface IConfigRegistrar {
        public void registerCustomAnnotations(IConfigRegistry registry);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigBoolean {
        String category();

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigBooleanArray {
        String category();

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigInteger {
        String category();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigIntegerArray {
        String category();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigFloat {
        String category();

        float minValue() default Float.MIN_VALUE;

        float maxValue() default Float.MAX_VALUE;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigDouble {
        String category();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigDoubleArray {
        String category();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigString {
        String category();

        String[] validValues() default {};

        String[] comment() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigStringArray {
        String category();

        String[] comment() default {};
    }
}
