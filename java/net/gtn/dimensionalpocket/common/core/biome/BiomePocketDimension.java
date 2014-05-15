package net.gtn.dimensionalpocket.common.core.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomePocketDimension extends BiomeGenBase {

    public BiomePocketDimension(int par1) {
        super(par1);
        setBiomeName("Pocket Dimension");
        setDisableRain();
    }

    @Override
    public boolean canSpawnLightningBolt() {
        return false;
    }
}
