package net.gtn.dimensionalpocket.common.core.pocket;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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

    public Pocket(CoordSet chunkCoords, int blockDim, CoordSet blockCoords) {
        setBlockDim(blockDim);
        setBlockCoords(blockCoords);
        this.chunkCoords = chunkCoords;

        spawnSet = new CoordSet(1, 1, 1);
    }

    public int getSideState(World world, ForgeDirection side) {
        int powerLevel = world.isBlockProvidingPowerTo(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ(), side.getOpposite().ordinal());

        DPLogger.info(powerLevel);

        return powerLevel;
    }

    public void generatePocket(World world) {
        if (generated)
            return;

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

        ChunkLoaderHandler.addPocketChunkToLoader(world, this);
        generated = world.getBlock((chunkCoords.getX() * 16) + 1, chunkCoords.getY() * 16, (chunkCoords.getZ() * 16) + 1) == ModBlocks.dimensionalPocketFrame;
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

        if (dimID != Reference.DIMENSION_ID)
            PocketTeleporter.transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        else
            teleporter.placeInPortal(player, 0, 0, 0, 0);

        generatePocket(player.worldObj);
        return true;
    }

    public boolean teleportFrom(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return false;
        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        Pocket pocket = PocketRegistry.getPocket(chunkCoords);

        if (pocket == null)
            return false;

        int dimID = pocket.getBlockDim();

        if (isSourceBlockPlaced(dimID, blockCoords)) {
            PocketTeleporter teleporter = PocketTeleporter.createTeleporter(dimID, pocket.getBlockCoords());

            if (dimID != Reference.DIMENSION_ID)
                PocketTeleporter.transferPlayerToDimension(player, dimID, teleporter);
            else
                teleporter.placeInPortal(player, 0, 0, 0, 0);

        } else {
            ChatComponentTranslation comp = new ChatComponentTranslation("info.trapped");
            comp.getChatStyle().setItalic(true);
            entityPlayer.addChatMessage(comp);
        }

        return true;
    }

    private boolean isSourceBlockPlaced(int dimID, CoordSet blockCoords) {
        Block block = MinecraftServer.getServer().worldServerForDimension(dimID).getBlock(blockCoords.getX(), blockCoords.getY(), blockCoords.getZ());

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

    public boolean setSpawnSet(CoordSet spawnSet) {
        if (spawnSet.getY() <= 1)
            this.spawnSet = spawnSet;

        boolean flag = this.spawnSet.equals(spawnSet);
        
        
        
        return flag;
    }

    public void setBlockCoords(CoordSet blockCoords) {
        this.blockCoords = blockCoords;
    }
}
