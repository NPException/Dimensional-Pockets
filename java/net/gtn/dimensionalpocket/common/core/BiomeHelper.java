package net.gtn.dimensionalpocket.common.core;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeHelper {

    private static BiomeGenBase pocketBiome;
    static boolean init = false;

    public static void init() {
        if (init)
            return;

        pocketBiome = new BiomeGenBase(99) {
            @Override
            public boolean canSpawnLightningBolt() {
                return false;
            }
        }.setBiomeName("Pocket Dimension").setDisableRain();

        BiomeDictionary.registerBiomeType(pocketBiome, Type.MAGICAL);
    }

    public static BiomeGenBase getPocketBiome() {
        return pocketBiome;
    }
}
