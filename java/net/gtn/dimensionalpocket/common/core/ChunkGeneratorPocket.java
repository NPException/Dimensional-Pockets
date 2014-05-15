package net.gtn.dimensionalpocket.common.core;

import java.util.List;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderFlat;

import com.google.common.collect.Lists;

public class ChunkGeneratorPocket implements IChunkProvider {

    private World worldObj;
    private Block[] baseArray = new Block[65536];

    public ChunkGeneratorPocket(World worldObj) {
        this.worldObj = worldObj;
    }

    @Override
    public boolean chunkExists(int var1, int var2) {
        return true;
    }

    @Override
    public Chunk loadChunk(int x, int z) {
        return provideChunk(x, z);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = new Chunk(worldObj, x, z);
        chunk.generateSkylightMap();

        Block[] tempArray = baseArray.clone();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k <= 255; k++) {
                    int l = k >> 4;
                    ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];

                    if (extendedBlockStorage == null) {
                        extendedBlockStorage = new ExtendedBlockStorage(k, !worldObj.provider.hasNoSky);
                        chunk.getBlockStorageArray()[l] = extendedBlockStorage;
                    }

                    Block block = Blocks.air;
                    if (i == 0 || i == 15 || j == 0 || j == 15 || (k % 16) == 0 || (k % 16) == 15)
                        block = ModBlocks.dimensionalPocketFrame;
                    tempArray[((i * 16 + j) * 256 + k)] = block;

                    extendedBlockStorage.setExtSkylightValue(i, k & 0x0F, j, 1);
                    extendedBlockStorage.func_150818_a(i, k & 0x0F, j, block);
                    extendedBlockStorage.setExtBlockMetadata(i, k & 0x0F, j, 0);
                }
            }
        }

//        chunk.generateSkylightMap();

//        chunk.setBiomeArray(new byte[] { (byte) BiomeHelper.getPocketBiome().biomeID });

        chunk.generateSkylightMap();
        return chunk;
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
