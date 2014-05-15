package net.gtn.dimensionalpocket.common.core;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderPocket extends WorldProvider {

    @Override
    public IChunkProvider createChunkGenerator() {
        return super.createChunkGenerator();
    }
    
    @Override
    public boolean canRespawnHere() {
        return true;
    }
    
    @Override
    public boolean canDoLightning(Chunk chunk) {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return false;
    }

    @Override
    public String getDimensionName() {
        return "PocketDimension";
    }

}
