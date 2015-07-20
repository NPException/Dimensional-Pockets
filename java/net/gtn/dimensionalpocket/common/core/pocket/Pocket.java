package net.gtn.dimensionalpocket.common.core.pocket;

import static net.gtn.dimensionalpocket.DPAnalytics.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.TeleportDirection;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;


public class Pocket {

	// NBT CONSTANTS START //
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocketData";
	private static final String NBT_FLOW_STATE_MAP_KEY = "stateMap";
	private static final String NBT_CONNECTOR_MAP_KEY = "connectorMap";

	private static final String NBT_GENERATED_KEY = "generated";
	private static final String NBT_BLOCK_DIMENSION_KEY = "blockDim";
	private static final String NBT_CHUNK_COORDS_KEY = "chunkCoords";
	private static final String NBT_BLOCK_COORDS_KEY = "blockCoords";
	private static final String NBT_SPAWN_COORDS_KEY = "spawnCoords";
	private static final String NBT_SPAWN_COORDS_YAW_KEY = "spawnCoordsYaw";
	private static final String NBT_SPAWN_COORDS_PITCH_KEY = "spawnCoordsPitch";
	private static final String NBT_CREATOR_KEY = "creator";
	// NBT CONSTANTS END //

	private transient NBTTagCompound nbtTagCompound;

	@SerializedName("connectors")
	private Map<ForgeDirection, CoordSet> connectorMap;

	@SerializedName("sideStates")
	private Map<ForgeDirection, PocketSideState> stateMap;

	@SerializedName("generated")
	private boolean isGenerated = false;

	@SerializedName("blockDim")
	private int blockDim;

	@SerializedName("chunkCoords")
	private CoordSet chunkCoords;

	@SerializedName("blockCoords")
	private CoordSet blockCoords;

	@SerializedName("spawnCoords")
	private CoordSet spawnCoords;
	@SerializedName("spawnCoordsYaw")
	private float spawnYaw;
	@SerializedName("spawnCoordsPitch")
	private float spawnPitch;

	@SerializedName("creator")
	private String creator;

	@Deprecated
	private CoordSet spawnSet; // renamed to spawnCoords. Needs to stay for compatibility with old saves.

	private Map<ForgeDirection, CoordSet> getConnectorMap() {
		if (connectorMap == null) {
			connectorMap = new EnumMap<>(ForgeDirection.class);
		}
		return connectorMap;
	}

	private Map<ForgeDirection, PocketSideState> getSideStateMap() {
		if (stateMap == null) {
			stateMap = new EnumMap<>(ForgeDirection.class);
		}
		return stateMap;
	}

	private Pocket() {
		// only used for reading from nbt
	}

	public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
		setBlockDim(blockDim);
		setBlockCoords(blockCoords);
		setSpawnInPocket(new CoordSet(7, 1, 7), 0f, 0f);
		this.chunkCoords = chunkCoords;
	}

	public void generatePocketRoom(String creatorName) {
		if (isGenerated)
			return;

		World world = PocketRegistry.getWorldForPockets();

		int worldX = chunkCoords.x * 16;
		int worldY = chunkCoords.y * 16;
		int worldZ = chunkCoords.z * 16;

		Chunk chunk = world.getChunkFromChunkCoords(chunkCoords.x, chunkCoords.z);

		int l = worldY >> 4;
		ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];

		if (extendedBlockStorage == null) {
			extendedBlockStorage = new ExtendedBlockStorage(worldY, !world.provider.hasNoSky);
			chunk.getBlockStorageArray()[l] = extendedBlockStorage;
		}

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					boolean flagX = x == 0 || x == 15;
					boolean flagY = y == 0 || y == 15;
					boolean flagZ = z == 0 || z == 15;

					// Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || (flagX && (flagY || flagZ)) || (flagY && flagZ)) {
						continue;
					}

					extendedBlockStorage.func_150818_a(x, y, z, ModBlocks.dimensionalPocketWall);
					world.markBlockForUpdate(worldX + x, worldY + y, worldZ + z);
				}
			} // @Jezza please do me the favor and let me have these brackets...
		}

		isGenerated = world.getBlock(worldX + 1, worldY, worldZ + 1) instanceof BlockDimensionalPocketWall;
		getNBT().setBoolean(NBT_GENERATED_KEY, isGenerated);

		if (!Strings.isNullOrEmpty(creatorName)) {
			creator = creatorName;
			getNBT().setString(NBT_GENERATED_KEY, creatorName);
		}

		generateDefaultConnectors();
	}

	public void resetFlowStates() {
		getSideStateMap().clear();
		getNBT().setTag(NBT_FLOW_STATE_MAP_KEY, new NBTTagCompound());
	}

	public PocketSideState getFlowState(ForgeDirection side) {
		Map<ForgeDirection, PocketSideState> fMap = getSideStateMap();
		return fMap.containsKey(side) ? fMap.get(side) : PocketSideState.NONE;
	}

	public void setFlowState(ForgeDirection side, PocketSideState flowState) {
		Utils.enforceServer();

		if (flowState != PocketSideState.NONE) {
			getSideStateMap().put(side, flowState);
			getNBT().getCompoundTag(NBT_FLOW_STATE_MAP_KEY).setString(side.name(), flowState.name());
		} else {
			getSideStateMap().remove(side);
			getNBT().getCompoundTag(NBT_FLOW_STATE_MAP_KEY).removeTag(side.name());
		}

		markConnectorForUpdate(side);
		markSourceBlockForUpdate();
	}

	public void markConnectorForUpdate(ForgeDirection side) {
		World world = PocketRegistry.getWorldForPockets();
		CoordSet connectorCoords = getConnectorCoords(side);

		if (connectorCoords != null) {
			int conX = connectorCoords.x;
			int conY = connectorCoords.y;
			int conZ = connectorCoords.z;
			if (world.blockExists(conX, conY, conZ)) {
				world.markBlockForUpdate(conX, conY, conZ);
				world.notifyBlockChange(conX, conY, conZ, ModBlocks.dimensionalPocketWall);
			}
		}
	}

	public void markSourceBlockForUpdate() {
		if (isSourceBlockPlaced()) {
			CoordSet srcCoords = getBlockCoords();
			World blockWorld = getBlockWorld();
			int srcX = srcCoords.x;
			int srcY = srcCoords.y;
			int srcZ = srcCoords.z;
			if (blockWorld.blockExists(srcX, srcY, srcZ)) {
				blockWorld.markBlockForUpdate(srcX, srcY, srcZ);
				blockWorld.notifyBlockChange(srcX, srcY, srcZ, ModBlocks.dimensionalPocket);
			}
		}
	}

	private void generateDefaultConnectors() {
		Utils.enforceServer();
		CoordSet root = chunkCoords.toBlockCoords();
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			switch (side) {
				case DOWN:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(7, 0, 7)));
					break;
				case UP:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(7, 15, 7)));
					break;
				case NORTH:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(7, 7, 0)));
					break;
				case SOUTH:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(7, 7, 15)));
					break;
				case WEST:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(0, 7, 7)));
					break;
				case EAST:
					setConnectorForSide(side, root.copy().addCoordSet(new CoordSet(15, 7, 7)));
					break;
				default:
					break;
			}
		}
	}

	public CoordSet getConnectorCoords(ForgeDirection side) {
		Map<ForgeDirection, CoordSet> cMap = getConnectorMap();
		if (cMap.size() < 6) {
			generateDefaultConnectors();
		}

		CoordSet connectorCoords = cMap.get(side);
		return (connectorCoords == null) ? null : connectorCoords.copy();
	}

	/**
	 * Attempts to set a new connector on a wall for a pocket. Returns whether it
	 * successfully set the new coords.
	 */
	public boolean setConnectorForSide(ForgeDirection side, CoordSet connectorCoords) {
		Utils.enforceServer();

		World world = PocketRegistry.getWorldForPockets();
		if (ModBlocks.dimensionalPocketWall != connectorCoords.getBlock(world))
			return false;

		if (getConnectorMap().size() == 6) { // the check is needed to prevent a stack overflow on first connector creation
			CoordSet oldCoords = getConnectorCoords(side);
			if (oldCoords != null) {
				TileEntity te = oldCoords.getTileEntity(world);
				if (te instanceof TileDimensionalPocketWallConnector) {
					((TileDimensionalPocketWallConnector) te).invalidateConnector();
				}
			}
		}

		getConnectorMap().put(side, connectorCoords);
		connectorCoords.writeToNBT(getNBT().getCompoundTag(NBT_CONNECTOR_MAP_KEY), side.name());

		world.setBlockMetadataWithNotify(connectorCoords.x, connectorCoords.y, connectorCoords.z, BlockDimensionalPocketWall.CONNECTOR_META, 3);
		world.markBlockForUpdate(connectorCoords.x, connectorCoords.y, connectorCoords.z);
		world.notifyBlockChange(connectorCoords.x, connectorCoords.y, connectorCoords.z, ModBlocks.dimensionalPocketWall);

		if (isSourceBlockPlaced()) {
			CoordSet srcCoords = getBlockCoords();
			World blockWorld = getBlockWorld();
			blockWorld.markBlockForUpdate(srcCoords.x, srcCoords.y, srcCoords.z);
			blockWorld.notifyBlockChange(srcCoords.x, srcCoords.y, srcCoords.z, ModBlocks.dimensionalPocket);
		}

		return true;
	}

	public void teleportTo(EntityPlayer entityPlayer) {
		if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
			return;

		World world = getBlockWorld();
		TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, blockCoords.x, blockCoords.y, blockCoords.z);
		if (teleportSide == TeleportDirection.UNKNOWN) {
			ChatComponentTranslation comp = new ChatComponentTranslation("info.exit.blocked");
			comp.getChatStyle().setItalic(Boolean.TRUE);
			entityPlayer.addChatMessage(comp);
			if (analytics.isActive()) {
				analytics.logPlayerTrappedOutsideEvent();
			}
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

		int dimID = player.dimension;

		CoordSet tempSet = chunkCoords.copy();
		tempSet.asBlockCoords();
		tempSet.addCoordSet(getSpawnCoords());

		PocketTeleporter teleporter = PocketTeleporter.createTeleporter(dimID, tempSet, spawnYaw, spawnPitch);

		generatePocketRoom(entityPlayer.getCommandSenderName());

		if (dimID != Reference.DIMENSION_ID) {
			PocketTeleporter.transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
		} else {
			teleporter.placeInPortal(player, 0, 0, 0, 0);
		}

		if (analytics.isActive()) {
			analytics.logPlayerTeleportInEvent();
		}
	}

	public void teleportFrom(EntityPlayer entityPlayer) {
		if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
			return;

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
		World world = getBlockWorld();

		if (isSourceBlockPlaced()) {
			TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, blockCoords.x, blockCoords.y, blockCoords.z);
			if (teleportSide != TeleportDirection.UNKNOWN) {
				CoordSet tempBlockSet = blockCoords.copy().addCoordSet(teleportSide.toCoordSet());
				tempBlockSet.y += -1;
				PocketTeleporter teleporter = PocketTeleporter.createTeleporter(blockDim, tempBlockSet, player.rotationYaw, player.rotationPitch);

				if (blockDim != Reference.DIMENSION_ID) {
					PocketTeleporter.transferPlayerToDimension(player, blockDim, teleporter);
				} else {
					teleporter.placeInPortal(player, 0, 0, 0, 0);
				}

				if (analytics.isActive()) {
					analytics.logPlayerTeleportOutEvent();
				}
			} else {
				ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped.2");
				comp.getChatStyle().setItalic(Boolean.TRUE);
				entityPlayer.addChatMessage(comp);
				if (analytics.isActive()) {
					analytics.logPlayerTrappedBlockedEvent();
				}
			}
		} else {
			ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped.1");
			comp.getChatStyle().setItalic(Boolean.TRUE);
			entityPlayer.addChatMessage(comp);
			if (analytics.isActive()) {
				analytics.logPlayerTrappedNotPlacedEvent();
			}
		}
	}

	public boolean isSourceBlockPlaced() {
		return getBlock() instanceof BlockDimensionalPocket;
	}

	public World getBlockWorld() {
		return MinecraftServer.getServer().worldServerForDimension(blockDim);
	}

	public Block getBlock() {
		World world = getBlockWorld();
		if (world == null) {
			DPLogger.warning("Dimension with ID " + blockDim + " does not exist... (Mystcraft or GalactiCraft world?) Returning null for Pocket");
			return null;
		}
		return world.getBlock(blockCoords.x, blockCoords.y, blockCoords.z);
	}

	public int getBlockDim() {
		return blockDim;
	}

	public void setBlockDim(int blockDim) {
		this.blockDim = blockDim;
		getNBT().setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);
	}

	public CoordSet getBlockCoords() {
		return blockCoords.copy();
	}

	public void setBlockCoords(CoordSet blockCoords) {
		this.blockCoords = blockCoords;
		this.blockCoords.writeToNBT(getNBT(), NBT_BLOCK_COORDS_KEY);
	}

	private CoordSet getSpawnCoords() {
		if (spawnCoords == null) {
			spawnCoords = spawnSet;
			spawnSet = null;
		}
		return spawnCoords;
	}

	public void setSpawnInPocket(CoordSet spawnCoords, float spawnYaw, float spawnPitch) {
		this.spawnCoords = spawnCoords;
		this.spawnCoords.writeToNBT(getNBT(), NBT_SPAWN_COORDS_KEY);

		this.spawnYaw = spawnYaw;
		getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, this.spawnYaw);
		this.spawnPitch = spawnPitch;
		getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, this.spawnPitch);
	}

	public CoordSet getChunkCoords() {
		return chunkCoords.copy();
	}

	public String getCreator() {
		return creator;
	}

	public static ForgeDirection getSideForConnector(CoordSet coordSet) {
		ForgeDirection direction = ForgeDirection.UNKNOWN;

		if (coordSet.x == 0)
			return ForgeDirection.WEST;
		if (coordSet.x == 15)
			return ForgeDirection.EAST;
		if (coordSet.y == 0)
			return ForgeDirection.DOWN;
		if (coordSet.y == 15)
			return ForgeDirection.UP;
		if (coordSet.z == 0)
			return ForgeDirection.NORTH;
		if (coordSet.z == 15)
			return ForgeDirection.SOUTH;

		return direction;
	}

	private NBTTagCompound getNBT() {
		if (nbtTagCompound == null) {
			// generate first compound
			nbtTagCompound = new NBTTagCompound();

			NBTTagCompound stateMap = new NBTTagCompound();
			for (Entry<ForgeDirection, PocketSideState> entry : getSideStateMap().entrySet()) {
				ForgeDirection side = entry.getKey();
				PocketSideState state = entry.getValue();
				stateMap.setString(side.name(), state.name());
			}
			nbtTagCompound.setTag(NBT_FLOW_STATE_MAP_KEY, stateMap);

			NBTTagCompound connectorMap = new NBTTagCompound();
			for (Entry<ForgeDirection, CoordSet> entry : getConnectorMap().entrySet()) {
				ForgeDirection side = entry.getKey();
				CoordSet connectorCoords = entry.getValue();
				connectorCoords.writeToNBT(connectorMap, side.name());
			}
			nbtTagCompound.setTag(NBT_CONNECTOR_MAP_KEY, connectorMap);

			nbtTagCompound.setBoolean(NBT_GENERATED_KEY, isGenerated);
			nbtTagCompound.setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);

			if (chunkCoords != null) {
				chunkCoords.writeToNBT(nbtTagCompound, NBT_CHUNK_COORDS_KEY);
			}

			if (blockCoords != null) {
				blockCoords.writeToNBT(nbtTagCompound, NBT_BLOCK_COORDS_KEY);
			}

			if (getSpawnCoords() != null) {
				spawnCoords.writeToNBT(nbtTagCompound, NBT_SPAWN_COORDS_KEY);
			}

			getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, spawnYaw);
			getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, spawnPitch);

			if (creator != null && !creator.isEmpty()) {
				nbtTagCompound.setString(NBT_CREATOR_KEY, creator);
			}
		}

		return nbtTagCompound;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setTag(NBT_DIMENSIONAL_POCKET_KEY, getNBT());
	}

	public static Pocket readFromNBT(NBTTagCompound tag) {
		// this is for staying compatible with older saves prior to version 0.10.0
		CoordSet oldVersionChunkCoords = CoordSet.readFromNBT(tag);
		if (oldVersionChunkCoords != null)
			return PocketRegistry.getPocket(oldVersionChunkCoords);

		NBTTagCompound pocketTag = tag.getCompoundTag(NBT_DIMENSIONAL_POCKET_KEY);

		Pocket pocket = new Pocket();

		pocket.chunkCoords = CoordSet.readFromNBT(pocketTag, NBT_CHUNK_COORDS_KEY);
		pocket.blockDim = pocketTag.getInteger(NBT_BLOCK_DIMENSION_KEY);
		pocket.blockCoords = CoordSet.readFromNBT(pocketTag, NBT_BLOCK_COORDS_KEY);

		pocket.isGenerated = pocketTag.getBoolean(NBT_GENERATED_KEY);
		pocket.spawnCoords = CoordSet.readFromNBT(pocketTag, NBT_SPAWN_COORDS_KEY);
		pocket.spawnYaw = pocketTag.getFloat(NBT_SPAWN_COORDS_YAW_KEY);
		pocket.spawnPitch = pocketTag.getFloat(NBT_SPAWN_COORDS_PITCH_KEY);

		pocket.creator = pocketTag.getString(NBT_CREATOR_KEY);
		if (pocket.creator.isEmpty()) {
			pocket.creator = null;
		}

		// basic values must be read out first, so that they wont get lost on NBT generation
		// while putting in the states

		NBTTagCompound stateMap = pocketTag.getCompoundTag(NBT_FLOW_STATE_MAP_KEY);
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			if (stateMap.hasKey(side.name())) {
				PocketSideState state = PocketSideState.valueOf(stateMap.getString(side.name()));
				pocket.getSideStateMap().put(side, state);
			}
		}

		NBTTagCompound connectorMap = pocketTag.getCompoundTag(NBT_CONNECTOR_MAP_KEY);
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			if (connectorMap.hasKey(side.name())) {
				CoordSet connectorCoords = CoordSet.readFromNBT(connectorMap, side.name());
				pocket.getConnectorMap().put(side, connectorCoords);
			}
		}

		return pocket;
	}
}
