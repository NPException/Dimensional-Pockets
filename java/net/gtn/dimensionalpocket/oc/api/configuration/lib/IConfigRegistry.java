package net.gtn.dimensionalpocket.oc.api.configuration.lib;

import net.gtn.dimensionalpocket.oc.api.configuration.ConfigEntry;

import java.lang.annotation.Annotation;

public interface IConfigRegistry {

    /**
     * Only call this when you've had the chance from the interface.
     * To use this, implement {@link net.gtn.dimensionalpocket.oc.api.configuration.Config.IConfigRegistrar} on your main mod file.
     */
    public boolean registerAnnotation(final Class<? extends Annotation> clazz, final Class<? extends ConfigEntry<? extends Annotation, ?>> configEntry);

}
