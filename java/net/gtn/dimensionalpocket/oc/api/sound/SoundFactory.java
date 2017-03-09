package net.gtn.dimensionalpocket.oc.api.sound;

import cpw.mods.fml.common.Loader;

public class SoundFactory {

    public static ISound createSound(String soundName) {
        return new Sound(Loader.instance().activeModContainer().getModId(), soundName);
    }

    public static ISound createSound(String modID, String soundName) {
        return new Sound(modID, soundName);
    }

}
