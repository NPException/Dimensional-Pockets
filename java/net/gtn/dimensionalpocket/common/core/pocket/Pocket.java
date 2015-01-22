package net.gtn.dimensionalpocket.common.core.pocket;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.annotations.SerializedName;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.utils.TeleportDirection;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

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
    // NBT CONSTANTS END //
    
    private transient NBTTagCompound nbtTagCompound;

    @SerializedName("connectors")
    private Map<ForgeDirection, CoordSet> connectorMap;
    
    @SerializedName("sideStates")
    private Map<ForgeDirection, FlowState> flowMap; // TODO: rename to stateMap before release!

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
    
    @Deprecated
    private CoordSet spawnSet; // renamed to spawnCoords. Needs to stay for compatibility with old saves.

    private Map<ForgeDirection, CoordSet> getConnectorMap() {
    	if (connectorMap == null)
    		connectorMap = new EnumMap<ForgeDirection, CoordSet>(ForgeDirection.class);
    	return connectorMap;
    }
    
    private Map<ForgeDirection, FlowState> getFlowMap() {
    	if (flowMap == null)
    		flowMap = new EnumMap<ForgeDirection, FlowState>(ForgeDirection.class);
    	return flowMap;
    }

    private Pocket() {
        // only used for reading from nbt
    }

    public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        setSpawnCoords(new CoordSet(7, 1, 7));
        this.chunkCoords = chunkCoords;
    }

    public void generatePocketRoom() {
        if (isGenerated)
            return;

        World world = PocketRegistry.getWorldForPockets();

        int worldX = chunkCoords.getX() * 16;
        int worldY = chunkCoords.getY() * 16;
        int worldZ = chunkCoords.getZ() * 16;

        Chunk chunk = world.getChunkFromChunkCoords(chunkCoords.getX(), chunkCoords.getZ());

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

                    // Made these flags, so I could add these checks, almost halves it in time.
                    if (!(flagX || flagY || flagZ) || (flagX && (flagY || flagZ)) || (flagY && flagZ))
                        continue;

                    extendedBlockStorage.func_150818_a(x, y, z, ModBlocks.dimensionalPocketFrame);
                    world.markBlockForUpdate(worldX + x, worldY + y, worldZ + z);

                    // use that method if setting things in the chunk will cause problems in the future
                    // world.setBlock(worldX+x, worldY+y, worldZ+z, ModBlocks.dimensionalPocketFrame);
                }
            } // @Jezza please do me the favor and let me have these brackets...
        }

        isGenerated = world.getBlock((chunkCoords.getX() * 16) + 1, chunkCoords.getY() * 16, (chunkCoords.getZ() * 16) + 1) instanceof BlockDimensionalPocketFrame;
        getNBT().setBoolean(NBT_GENERATED_KEY, isGenerated);
    }

    public void resetFlowStates() {
    	getFlowMap().clear();
    	getNBT().setTag(NBT_FLOW_STATE_MAP_KEY, new NBTTagCompound());
    }

    public FlowState getFlowState(ForgeDirection side) {
    	Map<ForgeDirection, FlowState> fMap = getFlowMap();
        if (fMap.containsKey(side))
            return fMap.get(side);
        return FlowState.NONE;
    }

    public void setFlowState(ForgeDirection side, FlowState flowState) {
    	getFlowMap().put(side, flowState);
    	getNBT().getCompoundTag(NBT_FLOW_STATE_MAP_KEY).setString(side.name(), flowState.name());
    }
    
    private void generateDefaultConnectors() {
        Utils.enforceServer();
        CoordSet root = chunkCoords.toBlockCoords();
        for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
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
        if (cMap.isEmpty())
            generateDefaultConnectors();
        return cMap.get(side);
    }

    /**
     * Attempts to set a new connector on a wall for a pocket.
     * Returns whether it successfully set the new coords.
     * @return
     */
    public boolean setConnectorForSide(ForgeDirection side, CoordSet connectorCoords) {
        Utils.enforceServer();

        World world = PocketRegistry.getWorldForPockets();
        if (ModBlocks.dimensionalPocketFrame != connectorCoords.getBlock(world))
            return false;
        
        world.setBlockMetadataWithNotify( connectorCoords.getX(),connectorCoords.getY(), connectorCoords.getZ(),
                                          BlockDimensionalPocketFrame.CONNECTOR_META, 3 );
        
    	getConnectorMap().put(side, connectorCoords);
    	connectorCoords.writeToNBT( getNBT().getCompoundTag(NBT_CONNECTOR_MAP_KEY), side.name());
    	return true;
    }

    public boolean teleportTo(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return false;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;

        CoordSet tempSet = chunkCoords.copy();
        tempSet.asBlockCoords();
        tempSet.addCoordSet(getSpawnCoords());

        PocketTeleporter teleporter = PocketTeleporter.createTeleporter(dimID, tempSet);

        generatePocketRoom();

        if (dimID != Reference.DIMENSION_ID)
            PocketTeleporter.transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        else
            teleporter.placeInPortal(player, 0, 0, 0, 0);

        return true;
    }

    public boolean teleportFrom(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return false;
        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
        World world = getBlockWorld();

        if (isSourceBlockPlaced()) {
            TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());
            if (teleportSide != TeleportDirection.UNKNOWN) {
                CoordSet tempBlockSet = blockCoords.copy().addCoordSet(teleportSide.toCoordSet()).addY(-1);
                PocketTeleporter teleporter = PocketTeleporter.createTeleporter(blockDim, tempBlockSet);

                if (blockDim != Reference.DIMENSION_ID)
                    PocketTeleporter.transferPlayerToDimension(player, blockDim, teleporter);
                else
                    teleporter.placeInPortal(player, 0, 0, 0, 0);
            } else {
                ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped.2");
                comp.getChatStyle().setItalic(Boolean.TRUE);
                entityPlayer.addChatMessage(comp);
            }
        } else {
            ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped.1");
            comp.getChatStyle().setItalic(Boolean.TRUE);
            entityPlayer.addChatMessage(comp);
        }

        return true;
    }

    public boolean isSourceBlockPlaced() {
        return getBlock() instanceof BlockDimensionalPocket;
    }

    public World getBlockWorld() {
        return MinecraftServer.getServer().worldServerForDimension(blockDim);
    }

    public Block getBlock() {
        return getBlockWorld().getBlock(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());
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
    
    /*
     * Purely for compatibility with 0.07.7 saves
     */
    @Deprecated
    private CoordSet getSpawnCoords() {
        if (spawnCoords == null) {
            spawnCoords = spawnSet;
            spawnSet = null;
        }
        return spawnCoords;
    }
    
    public void setSpawnCoords(CoordSet spawnCoords) {
        this.spawnCoords = spawnCoords;
        this.spawnCoords.writeToNBT(getNBT(), NBT_SPAWN_COORDS_KEY);
    }

    public CoordSet getChunkCoords() {
        return chunkCoords.copy();
    }

    

    public static ForgeDirection getSideForBlock(CoordSet coordSet) {
        ForgeDirection direction = ForgeDirection.UNKNOWN;

        if (coordSet.getX() == 0)
            return ForgeDirection.WEST;
        if (coordSet.getX() == 15)
            return ForgeDirection.EAST;
        if (coordSet.getY() == 0)
            return ForgeDirection.DOWN;
        if (coordSet.getY() == 15)
            return ForgeDirection.UP;
        if (coordSet.getZ() == 0)
            return ForgeDirection.NORTH;
        if (coordSet.getZ() == 15)
            return ForgeDirection.SOUTH;

        return direction;
    }
    
    
    
    private NBTTagCompound getNBT() {
        if (nbtTagCompound == null) {
            // generate first compound
            nbtTagCompound = new NBTTagCompound();
            
            NBTTagCompound stateMap = new NBTTagCompound();
            for (Entry<ForgeDirection, FlowState> entry : getFlowMap().entrySet()) {
                ForgeDirection side = entry.getKey();
                FlowState state = entry.getValue();
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
            
            if (chunkCoords != null)
                chunkCoords.writeToNBT(nbtTagCompound, NBT_CHUNK_COORDS_KEY);
            
            if (blockCoords != null)
                blockCoords.writeToNBT(nbtTagCompound, NBT_BLOCK_COORDS_KEY);
            
            if (getSpawnCoords() != null)
                spawnCoords.writeToNBT(nbtTagCompound, NBT_SPAWN_COORDS_KEY);
        }
        
        return nbtTagCompound;
    }

    
    public void writeToNBT(NBTTagCompound tag) {
        tag.setTag(NBT_DIMENSIONAL_POCKET_KEY, getNBT());
    }
    
    public static Pocket readFromNBT(NBTTagCompound tag) {
        // this is for staying compatible with older saves prior to version 0.10.0
        CoordSet oldVersionChunkCoords = CoordSet.readFromNBT(tag);
        if (oldVersionChunkCoords != null) {
            return PocketRegistry.getPocket(oldVersionChunkCoords);
        }
        
        NBTTagCompound pocketTag = tag.getCompoundTag(NBT_DIMENSIONAL_POCKET_KEY);
        
        Pocket pocket = new Pocket();
        
        pocket.chunkCoords =  CoordSet.readFromNBT(pocketTag, NBT_CHUNK_COORDS_KEY);
        pocket.blockDim = pocketTag.getInteger(NBT_BLOCK_DIMENSION_KEY);
        pocket.blockCoords = CoordSet.readFromNBT(pocketTag, NBT_BLOCK_COORDS_KEY);
        
        pocket.isGenerated = pocketTag.getBoolean(NBT_GENERATED_KEY);
        pocket.spawnCoords = CoordSet.readFromNBT(pocketTag, NBT_SPAWN_COORDS_KEY);
        
        NBTTagCompound stateMap = pocketTag.getCompoundTag(NBT_FLOW_STATE_MAP_KEY);
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (stateMap.hasKey(side.name())) {
                FlowState state = FlowState.valueOf(stateMap.getString(side.name()));
                pocket.getFlowMap().put(side, state);
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
