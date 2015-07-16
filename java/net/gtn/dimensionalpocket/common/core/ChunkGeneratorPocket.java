package net.gtn.dimensionalpocket.common.core;

import java.util.Arrays;
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

	private World worldObj;

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
		Chunk chunk = new Chunk(worldObj, new Block[] {}, new byte[] {}, x, z);

		byte[] byteArray = new byte[256];
		Arrays.fill(byteArray, (byte) BiomeHelper.getPocketBiome().biomeID);

		chunk.setBiomeArray(byteArray);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(IChunkProvider var1, int var2, int var3) {
		// do nothing
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
		// do nothing
	}

	@Override
	public void saveExtraData() {
		// do nothing
	}
}
