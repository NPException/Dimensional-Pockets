package net.gtn.dimensionalpocket.common.core;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeHelper {

    private static BiomeGenBase pocketBiome;
    static boolean init = false;

    public static void init() {
        if (init) {
            DPLogger.severe("Tried calling BiomeHelper.init() again!");
            return;
        }
        init = true;

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
