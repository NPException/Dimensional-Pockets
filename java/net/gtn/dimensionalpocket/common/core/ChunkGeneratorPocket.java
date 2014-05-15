package net.gtn.dimensionalpocket.common.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.collect.Lists;

public class ChunkGeneratorPocket implements IChunkProvider {

    World worldObj;

    public ChunkGeneratorPocket(World worldObj) {
        this.worldObj = worldObj;
    }

    @Override
    public boolean chunkExists(int var1, int var2) {
        return true;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Block[] blocks = new Block[65536];

        populateChunk(x, z, blocks);

        Chunk chunk = new Chunk(worldObj, blocks, x, z);
        return chunk;
    }

    public void populateChunk(int x, int z, Block[] blocks) {

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < worldObj.getHeight(); j++) {
                for (int k = 0; k < 16; k++) {
                        blocks[]
                }
            }
        }

    }

    @Override
    public Chunk loadChunk(int x, int z) {
        return provideChunk(x, z);
    }

    @Override
    public void populate(IChunkProvider var1, int var2, int var3) {

    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdate var2) {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public String makeString() {
        return null;
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
        return Lists.newArrayList();
    }

    @Override
    public ChunkPosition func_147416_a(World var1, String var2, int var3, int var4, int var5) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int var1, int var2) {

    }

    @Override
    public void saveExtraData() {

    }

}
