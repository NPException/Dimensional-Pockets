package net.gtn.dimensionalpocket.common.core.pocket;

import java.util.Collection;

import cpw.mods.fml.common.FMLCommonHandler;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;


public class PocketTeleporter extends Teleporter {

	private CoordSet targetSet;
	private float spawnYaw, spawnPitch;

	public PocketTeleporter(WorldServer worldServer, CoordSet targetSet, float spawnYaw, float spawnPitch) {
		super(worldServer);
		Utils.enforceServer();
		this.targetSet = targetSet.copy();
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;
	}

	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
		if (!(entity instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entity;

		double posX = targetSet.x + 0.5F;
		double posY = targetSet.y + 1;
		double posZ = targetSet.z + 0.5F;

		player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, spawnYaw, spawnPitch);
		player.fallDistance = 0;
	}

	static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet, float spawnYaw, float spawnPitch) {
		return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet, spawnYaw, spawnPitch);
	}

	/**
	 * Stripped down version of {@link ServerConfigurationManager#transferPlayerToDimension(EntityPlayerMP, int, Teleporter)}
	 * which doesn't do any special handling for teleports between dimensions.
	 * (like moving to spawn when teleporting from the End, or applying movement factors when teleporting to the Nether)
	 */
	public static void transferPlayerToDimension(EntityPlayerMP player, int targetDim, Teleporter teleporter) {
		int sourceDim = player.dimension;
		MinecraftServer mcServer = MinecraftServer.getServer();
		WorldServer sourceWorld = mcServer.worldServerForDimension(player.dimension);
		player.dimension = targetDim;
		WorldServer targetWorld = mcServer.worldServerForDimension(player.dimension);
		player.playerNetServerHandler.sendPacket(
				new S07PacketRespawn(player.dimension, targetWorld.difficultySetting,
						targetWorld.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
		sourceWorld.removePlayerEntityDangerously(player);
		player.isDead = false;

		transferEntityToWorld(player, sourceWorld, targetWorld, teleporter);

		ServerConfigurationManager serverConfigurationManager = MinecraftServer.getServer().getConfigurationManager();
		// remove player from source world and add to target world. load chunk with player
		serverConfigurationManager.func_72375_a(player, sourceWorld);
		player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.theItemInWorldManager.setWorld(targetWorld);

		serverConfigurationManager.updateTimeAndWeatherForPlayer(player, targetWorld);
		serverConfigurationManager.syncPlayerInventory(player);

		@SuppressWarnings("unchecked")
		Collection<PotionEffect> potionEffects = player.getActivePotionEffects();

		for (PotionEffect potioneffect : potionEffects) {
			player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, sourceDim, targetDim);
	}

	/**
	 * Stripped down version of {@link ServerConfigurationManager#transferEntityToWorld(Entity, int, WorldServer, WorldServer, Teleporter)}
	 * which does no special handling for teleports from an to the end.
	 */
	private static void transferEntityToWorld(Entity entityIn, WorldServer sourceWorld, WorldServer targetWorld, Teleporter teleporter) {
		double d0 = entityIn.posX;
		double d1 = entityIn.posZ;
		double d3 = entityIn.posX;
		double d4 = entityIn.posY;
		double d5 = entityIn.posZ;
		float f = entityIn.rotationYaw;

		sourceWorld.theProfiler.startSection("placing");
		d0 = MathHelper.clamp_int((int) d0, -29999872, 29999872);
		d1 = MathHelper.clamp_int((int) d1, -29999872, 29999872);

		if (entityIn.isEntityAlive()) {
			entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
			teleporter.placeInPortal(entityIn, d3, d4, d5, f);
			targetWorld.spawnEntityInWorld(entityIn);
			targetWorld.updateEntityWithOptionalForce(entityIn, false);
		}

		sourceWorld.theProfiler.endSection();

		entityIn.setWorld(targetWorld);
	}
}
