package net.gtn.dimensionalpocket.common.core.pocket;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.pocket.states.RedstoneStateHandler;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.TeleportDirection;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

public class Pocket {

    private boolean generated = false;
    private int blockDim;
    private final CoordSet chunkCoords;
    private CoordSet blockCoords, spawnSet;

    private RedstoneStateHandler redstoneStateHandler;

    public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        this.chunkCoords = chunkCoords;

        spawnSet = new CoordSet(1, 1, 1);
        redstoneStateHandler = new RedstoneStateHandler();
    }

    public RedstoneStateHandler getRedstoneState() {
        return redstoneStateHandler;
    }

    public int getExternalLight() {
        if (isSourceBlockPlaced()) {
            World world = getBlockWorld();
            TileEntity tileEntity = world.getTileEntity(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());

            if (tileEntity instanceof TileDimensionalPocket)
                return ((TileDimensionalPocket) tileEntity).getLightForPocket();
        }
        return 0;
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

        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 16; y++)
                for (int z = 0; z < 16; z++) {
                    boolean flagX = x == 0 || x == 15;
                    boolean flagY = y == 0 || y == 15;
                    boolean flagZ = z == 0 || z == 15;

                    // Made these flags, so I could add these checks, almost halves it in time.
                    if (!(flagX || flagY || flagZ) || (flagX && (flagY || flagZ)) || (flagY && (flagX || flagZ)) || (flagZ && (flagY || flagX)))
                        continue;

                    extendedBlockStorage.func_150818_a(x, y, z, ModBlocks.dimensionalPocketFrame);
                    world.markBlockForUpdate(worldX + x, worldY + y, worldZ + z);

                    // use that method if setting things in the chunk will cause problems in the future
                    // world.setBlock(worldX+x, worldY+y, worldZ+z, ModBlocks.dimensionalPocketFrame);
                }

        generated = world.getBlock((chunkCoords.getX() * 16) + 1, chunkCoords.getY() * 16, (chunkCoords.getZ() * 16) + 1) instanceof BlockDimensionalPocketFrame;
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
        side = side.getOpposite();
        getBlockWorld().notifyBlockOfNeighborChange(blockCoords.getX() + side.offsetX, blockCoords.getY() + side.offsetY, blockCoords.getZ() + side.offsetZ, ModBlocks.dimensionalPocket);
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
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + MIN, blockSet.getZ() + z, side);
                break;
            case UP:
                for (int x = MIN; x <= MAX; x++)
                    for (int z = MIN; z <= MAX; z++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + MAX, blockSet.getZ() + z, side);
                break;
            case NORTH:
                for (int x = MIN; x <= MAX; x++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + y, blockSet.getZ() + MIN, side);
                break;
            case SOUTH:
                for (int x = MIN; x <= MAX; x++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + x, blockSet.getY() + y, blockSet.getZ() + MAX, side);
                break;
            case WEST:
                for (int z = MIN; z <= MAX; z++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + MIN, blockSet.getY() + y, blockSet.getZ() + z, side);
                break;
            case EAST:
                for (int z = MIN; z <= MAX; z++)
                    for (int y = MIN; y <= MAX; y++)
                        forcePossibleUpdate(world, blockSet.getX() + MAX, blockSet.getY() + y, blockSet.getZ() + z, side);
                break;
            default:
                break;
        }
    }

    private void forcePossibleUpdate(World world, int x, int y, int z, ForgeDirection side) {
        // Don't want to notify air...
        if (world.isAirBlock(x, y, z))
            return;
        DPLogger.info(new CoordSet(x, y, z));
        // world.notifyBlockOfNeighborChange(x, y, z, ModBlocks.dimensionalPocketFrame);
        world.notifyBlockChange(x, y, z, ModBlocks.dimensionalPocketFrame);
    }

    public boolean isSourceBlockPlaced() {
        World world = getBlockWorld();
        Block block = world.getBlock(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());
        return block instanceof BlockDimensionalPocket;
    }

    public World getBlockWorld() {
        return MinecraftServer.getServer().worldServerForDimension(blockDim);
    }

    public int getBlockDim() {
        return blockDim;
    }

    public CoordSet getSpawnSet() {
        return spawnSet;
    }

    public CoordSet getBlockCoords() {
        return blockCoords;
    }

    public CoordSet getChunkCoords() {
        return chunkCoords;
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

    public Block getBlock() {
        return getBlockWorld().getBlock(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());
    }

    public void onNeighbourBlockChanged(TileDimensionalPocket tile, CoordSet coordSet, Block block) {
        redstoneStateHandler.onSideChange(this, tile, coordSet, block);
    }

    public void onNeighbourBlockChangedPocket(ForgeDirection direction, CoordSet coordSet, Block block) {
        redstoneStateHandler.onSidePocketChange(this, direction, coordSet, block);
    }
}
