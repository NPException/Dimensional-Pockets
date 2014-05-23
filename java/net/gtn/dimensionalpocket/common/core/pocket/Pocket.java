package net.gtn.dimensionalpocket.common.core.pocket;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
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

    // private int[] outputSignals;
    // private int[] inputSignals;

    public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        this.chunkCoords = chunkCoords;

        spawnSet = new CoordSet(1, 1, 1);
    }

    // private int[] getOutputSignals() {
    // if (outputSignals == null)
    // outputSignals = new int[6];
    // return outputSignals;
    // }
    //
    // private int[] getInputSignals() {
    // if (inputSignals == null)
    // inputSignals = new int[6];
    // return inputSignals;
    // }

    // public void setOutputSignal(int side, int strength) {
    // getOutputSignals()[side] = strength;
    // }
    //
    // public void setInputSignal(int side, int strength) {
    // getInputSignals()[side] = strength;
    // }
    //
    // public int getOutputSignal(int side) {
    // if (side > 5)
    // return 0;
    //
    // return getOutputSignals()[side];
    // }
    //
    // public int getInputSignal(int side) {
    // if (side > 5)
    // return 0;
    //
    // return getInputSignals()[side];
    // }

    public int getExternalLight() {
        if (isSourceBlockPlaced()) {
            World world = MinecraftServer.getServer().worldServerForDimension(blockDim);
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
        forcePocketUpdate();

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

        if (isSourceBlockPlaced()) {
            if (canTeleportOut()) {
                PocketTeleporter teleporter = PocketTeleporter.createTeleporter(blockDim, blockCoords);

                if (blockDim != Reference.DIMENSION_ID)
                    PocketTeleporter.transferPlayerToDimension(player, blockDim, teleporter);
                else
                    teleporter.placeInPortal(player, 0, 0, 0, 0);
            } else {
                ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped2");
                comp.getChatStyle().setItalic(Boolean.TRUE);
                entityPlayer.addChatMessage(comp);
            }
        } else {
            ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped");
            comp.getChatStyle().setItalic(Boolean.TRUE);
            entityPlayer.addChatMessage(comp);
        }

        return true;
    }

    /**
     * If they're stupid enough to get stuck in it, they deserve it.
     * 
     * @return
     */
    public boolean canTeleportOut() {
        World world = MinecraftServer.getServer().worldServerForDimension(blockDim);

        int x = blockCoords.getX();
        int y = blockCoords.getY() + 1;
        int z = blockCoords.getZ();

        return world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    public boolean isSourceBlockPlaced() {
        World world = MinecraftServer.getServer().worldServerForDimension(blockDim);
        Block block = world.getBlock(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());
        return (block instanceof BlockDimensionalPocket);
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

    public void forcePocketUpdate() {
        World world = PocketRegistry.getWorldForPockets();

        int x = chunkCoords.getX();
        int y = chunkCoords.getY();
        int z = chunkCoords.getZ();

        world.markBlockRangeForRenderUpdate(x * 16, y * 16, z * 16, x * 16 + 15, y * 16 + 15, z * 16 + 15);
    }

    /**
     * Mark the chunk for a render update.
     * 
     * @param direction
     */
    public void forceChunkUpdate() {
        World world = PocketRegistry.getWorldForPockets();

        int x = chunkCoords.getX();
        int z = chunkCoords.getZ();

        world.markBlockRangeForRenderUpdate(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
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
}
