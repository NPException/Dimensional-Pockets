package net.gtn.dimensionalpocket.common.core;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class PocketDimensionHelper {

    public static void teleportPlayerToPocket(EntityPlayer entityPlayer, CoordSet targetSet) {
        if (entityPlayer.worldObj.isRemote)
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;
        
        PocketTeleporter teleporter = new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), targetSet);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }
        
        if (player.worldObj.getBlock(targetSet.getX()*16, targetSet.getY()*16, targetSet.getZ()*16) != ModBlocks.dimensionalPocketFrame) {
            generatePocket(player.worldObj, targetSet);
        }
    }
    
    private static void generatePocket(World world, CoordSet targetSet) {
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!(x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15)) {
                        continue;
                    }
                    // set block
                }
            }
        }
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
    
    public static void teleportPlayerFromPocket(){
        
    }
}
