package net.gtn.dimensionalpocket.common.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderPocket extends WorldProvider {

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkGeneratorPocket(worldObj);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean getWorldHasVoidParticles() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
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
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return true;
    }

    @Override
    public String getDimensionName() {
        return "PocketDimension";
    }

}
