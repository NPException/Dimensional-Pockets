package net.gtn.dimensionalpocket.common.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class WorldProviderPocket extends WorldProvider {

	public WorldProviderPocket() {
		hasNoSky = true;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			setSkyRenderer(new IRenderHandler() {
				@Override
				@SideOnly(Side.CLIENT)
				public void render(float partialTicks, WorldClient world, Minecraft mc) {
					// do nothing
				}
			});
		}
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkGeneratorPocket(worldObj);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		return Vec3.createVectorHelper(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2) {
		return Vec3.createVectorHelper(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 drawClouds(float partialTicks) {
		return Vec3.createVectorHelper(0, 0, 0);
	}

	@Override
	public String getDepartMessage() {
		return "Leaving the Dimensional Pocket...";
	}

	@Override
	public String getWelcomeMessage() {
		return "Entering the Dimensional Pocket...";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) {
		return 0.0F;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		return BiomeHelper.getPocketBiome();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 600000F;
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
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2) {
		return new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
	}

	@Override
	public String getDimensionName() {
		return "PocketDimension";
	}

}
