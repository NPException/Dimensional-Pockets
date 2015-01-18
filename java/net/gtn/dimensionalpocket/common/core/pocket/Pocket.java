package net.gtn.dimensionalpocket.common.core.pocket;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.utils.TeleportDirection;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumMap;
import java.util.Map;

public class Pocket {

    private Map<ForgeDirection, CoordSet> connectorMap;
    private Map<ForgeDirection, FlowState> flowMap;

    private boolean generated = false;
    private int blockDim;
    private final CoordSet chunkCoords;
    private CoordSet blockCoords, spawnSet;

    public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        this.chunkCoords = chunkCoords;

        this.connectorMap = new EnumMap<ForgeDirection, CoordSet>(ForgeDirection.class);
        this.flowMap = new EnumMap<ForgeDirection, FlowState>(ForgeDirection.class);

        spawnSet = new CoordSet(1, 1, 1);
    }

    public void generatePocketRoom() {
        if (generated)
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

        generated = world.getBlock((chunkCoords.getX() * 16) + 1, chunkCoords.getY() * 16, (chunkCoords.getZ() * 16) + 1) instanceof BlockDimensionalPocketFrame;
    }

    public FlowState getFlowState(ForgeDirection direction) {
        if (flowMap.containsKey(direction))
            flowMap.get(direction);
        return FlowState.NONE;
    }

    public void setFlowState(ForgeDirection direction, FlowState flowState) {
        flowMap.put(direction, flowState);
    }

    public CoordSet getOffset(ForgeDirection direction) {
        if (connectorMap.containsKey(direction))
            connectorMap.get(direction);
        return null;
    }

    public void setOffset(ForgeDirection direction, CoordSet connector) {
        connectorMap.put(direction, connector);
    }

    public boolean teleportTo(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return false;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;

        CoordSet tempSet = chunkCoords.copy();

        tempSet.asBlockCoords();
        tempSet.addCoordSet(spawnSet);

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

    public void forceSideUpdate(ForgeDirection side) {
        int x = blockCoords.getX() + side.offsetX;
        int y = blockCoords.getY() + side.offsetY;
        int z = blockCoords.getZ() + side.offsetZ;
        World world = getBlockWorld();

        if (world.isAirBlock(x, y, z))
            return;
        world.notifyBlockOfNeighborChange(x, y, z, ModBlocks.dimensionalPocket);
    }

    public void forcePocketSideUpdate(ForgeDirection side) {
        World world = PocketRegistry.getWorldForPockets();
        if (world.isRemote)
            return;

        CoordSet blockSet = chunkCoords.toBlockCoords();

        int MIN = 1;
        int MAX = 14;

        switch (side) {
            case DOWN:
                for (int x = MIN; x <= MAX; x++)
                    for (int z = MIN; z <= MAX; z++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + MIN, blockSet.getZ() + z);
                break;
            case UP:
                for (int x = MIN; x <= MAX; x++)
                    for (int z = MIN; z <= MAX; z++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + MAX, blockSet.getZ() + z);
                break;
            case NORTH:
                for (int x = MIN; x <= MAX; x++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + y, blockSet.getZ() + MIN);
                break;
            case SOUTH:
                for (int x = MIN; x <= MAX; x++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + y, blockSet.getZ() + MAX);
                break;
            case WEST:
                for (int z = MIN; z <= MAX; z++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + MIN, blockSet.getY() + y, blockSet.getZ() + z);
                break;
            case EAST:
                for (int z = MIN; z <= MAX; z++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + MAX, blockSet.getY() + y, blockSet.getZ() + z);
                break;
            default:
                break;
        }
    }

    private void forcePossibleUpdate(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z))
            return;
        world.notifyBlockOfNeighborChange(x, y, z, ModBlocks.dimensionalPocketFrame);
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

    public CoordSet getBlockCoords() {
        return blockCoords.copy();
    }

    public CoordSet getChunkCoords() {
        return chunkCoords.copy();
    }

    public void setBlockDim(int blockDim) {
        this.blockDim = blockDim;
    }

    public void setSpawnSet(CoordSet spawnSet) {
        this.spawnSet = spawnSet;
    }

    public void setBlockCoords(CoordSet blockCoords) {
        this.blockCoords = blockCoords;
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

    public void onNeighbourBlockChanged(TileDimensionalPocket tile, CoordSet coordSet, Block block) {
    }

    public void onNeighbourBlockChangedPocket(ForgeDirection direction, CoordSet coordSet, Block block) {
    }
}
