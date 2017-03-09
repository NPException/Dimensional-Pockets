package net.gtn.dimensionalpocket.common.core.pocket;

import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;


public class PocketRegistry {
	private static Map<CoordSet, Pocket> backLinkMap = new HashMap<>();

	private static final int pocketChunkSpacing = 20;
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private CoordSet currentChunk = new CoordSet(0, 0, 0);
		private ForgeDirection nextPocketCoordsDirection = ForgeDirection.NORTH;
	}

	public static WorldServer getWorldForPockets() {
		return MinecraftServer.getServer().worldServerForDimension(Reference.DIMENSION_ID);
	}

	public static Pocket getPocket(CoordSet chunkCoords) {
		Utils.enforceServer();
		if (backLinkMap.containsKey(chunkCoords))
			return backLinkMap.get(chunkCoords);
		return null;
	}

	private static CoordSet getNextPocketCoords(CoordSet currentCoords) {
		CoordSet result = currentCoords;

		// step until an empty spot has been reached
		while (backLinkMap.containsKey(result)) {
			// create offset for next pocket
			CoordSet offset = new CoordSet().addForgeDirection(pocketGenParameters.nextPocketCoordsDirection);
			offset.x *= pocketChunkSpacing;
			offset.z *= pocketChunkSpacing;

			// create result coordset
			result = currentCoords.copy().addCoordSet(offset);
			// needed to bring old saves to this height
			result.y = 4;
		}

		// create test offset to check for the next pockets direction
		ForgeDirection clockwiseTurn = pocketGenParameters.nextPocketCoordsDirection.getRotation(ForgeDirection.UP);
		CoordSet probeOffset = new CoordSet().addForgeDirection(clockwiseTurn);
		probeOffset.x *= pocketChunkSpacing;
		probeOffset.z *= pocketChunkSpacing;

		// check if probed coordset is mapped to a pocket already
		CoordSet probeCoords = result.copy().addCoordSet(probeOffset);
		if (!backLinkMap.containsKey(probeCoords)) {
			pocketGenParameters.nextPocketCoordsDirection = clockwiseTurn;
		}

		return result;
	}

	public static Pocket getOrCreatePocket(World world, CoordSet coordSetSource) {
		Utils.enforceServer();

		int dimIDSource = world.provider.dimensionId;
		for (Pocket pocket : backLinkMap.values()) {
			if (pocket.getBlockDim() == dimIDSource && pocket.getBlockCoords().equals(coordSetSource))
				return pocket;
		}

		// only update currentChunkCoords if pockets already exist
		if (!backLinkMap.isEmpty()) {
			pocketGenParameters.currentChunk = getNextPocketCoords(pocketGenParameters.currentChunk);
		}

		Pocket pocket = new Pocket(pocketGenParameters.currentChunk.copy(), dimIDSource, coordSetSource);
		backLinkMap.put(pocket.getChunkCoords(), pocket);

		saveData();

		return pocket;
	}

	public static void updatePocket(CoordSet chunkCoords, int newBlockDimID, CoordSet newBlockCoords) {
		Utils.enforceServer();
		Pocket link = backLinkMap.get(chunkCoords);
		if (link == null) {
			DPLogger.severe("No Pocket for chunkCoords: " + chunkCoords);
			return;
		}

		link.setBlockDim(newBlockDimID);
		link.setBlockCoords(newBlockCoords);

		saveData();
	}

	public static void saveData() {
		PocketConfig.saveBackLinkMap(backLinkMap);
		PocketConfig.savePocketGenParams(pocketGenParameters);
	}

	public static void loadData() {
		backLinkMap = PocketConfig.loadBackLinkMap();
		pocketGenParameters = PocketConfig.loadPocketGenParams();
	}

	public static void validatePocketConnectors() {
		DPLogger.info("Enforcing valid metadata for wall connector blocks");
		World pocketWorld = getWorldForPockets();
		for (Pocket pocket : backLinkMap.values()) {
			// this call will generate the connectors if they do not yet exist
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				CoordSet coords = pocket.getConnectorCoords(side);
				if (coords != null) {
					Block block = coords.getBlock(pocketWorld);
					if (block == ModBlocks.dimensionalPocketWall) {
						pocketWorld.setBlockMetadataWithNotify(coords.x, coords.y, coords.z, BlockDimensionalPocketWall.CONNECTOR_META, 3);
					}
				}
			}
		}
	}

	public static void initChunkLoading() {
		for (Pocket pocket : backLinkMap.values()) {
			ChunkLoaderHandler.addPocketToChunkLoader(pocket);
		}
	}
}
