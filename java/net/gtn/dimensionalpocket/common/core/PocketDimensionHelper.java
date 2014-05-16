package net.gtn.dimensionalpocket.common.core;

import cpw.mods.fml.common.FMLCommonHandler;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportLink;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class PocketDimensionHelper {

    public static void teleportPlayerToPocket(EntityPlayer entityPlayer, CoordSet chunkSet) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;

        PocketTeleporter teleporter = createTeleporter(dimID, chunkSet, true);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }

        generatePocketIfNecessary(player.worldObj, chunkSet);
    }

    private static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet, boolean stepIntoPocket) {
        return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet, stepIntoPocket);
    }

    /**
     * Generates the new room. THATS why you hired me :D
     * 
     * @author NPException
     * @param world
     * @param chunkSet
     */
    private static void generatePocketIfNecessary(World world, CoordSet chunkSet) {
        if (world.getBlock((chunkSet.getX() * 16) + 1, chunkSet.getY() * 16, (chunkSet.getZ() * 16) + 1) == ModBlocks.dimensionalPocketFrame)
            return;

        int worldX = chunkSet.getX() * 16;
        int worldY = chunkSet.getY() * 16;
        int worldZ = chunkSet.getZ() * 16;

        Chunk chunk = world.getChunkFromChunkCoords(chunkSet.getX(), chunkSet.getZ());

        int l = worldY >> 4;
        ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];

        if (extendedBlockStorage == null) {
            extendedBlockStorage = new ExtendedBlockStorage(worldY, !world.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedBlockStorage;
        }

        // FULL GEN AVERAGE TIME: 505052.3125 nanoSeconds
        // EDGED GEN AVERAGE TIME: 318491.4375 nanoSeconds

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
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
            }
        }
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }

    public static void teleportPlayerFromPocket(EntityPlayer entityPlayer, CoordSet frameCoords) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        TeleportLink link = TeleportingRegistry.getLinkForPocketChunkCoords(frameCoords.copyDividedBy16());

        if (link == null)
            return;

        int dimID = link.getBlockDim();

        PocketTeleporter teleporter = createTeleporter(dimID, link.getBlockCoords(), false);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, dimID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }
    }
}
