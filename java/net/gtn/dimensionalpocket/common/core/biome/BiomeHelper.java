package net.gtn.dimensionalpocket.common.core.biome;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeHelper {

    private static BiomePocketDimension pocketBiome;
    static boolean init = false;

    public static void init() {
        if (init)
            return;

        pocketBiome = new BiomePocketDimension(99);
    }

    public static BiomePocketDimension getPocketBiome() {
        return pocketBiome;
    }

    public static void registerAll() {
        BiomeDictionary.registerBiomeType(pocketBiome, Type.MAGICAL);
    }

}
