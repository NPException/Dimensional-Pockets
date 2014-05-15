package net.gtn.dimensionalpocket.common.block;

import cpw.mods.fml.client.ExtendedServerListData;
import net.gtn.dimensionalpocket.common.core.DPLogger;
import net.gtn.dimensionalpocket.common.core.PocketTeleporter;
import net.gtn.dimensionalpocket.common.items.ItemBlockHolder;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDimensionalPocket extends BlockDP {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return true;

        if (!world.isRemote) {
            // TODO, ID shit
            int dimID = player.dimension;

            if (dimID != Reference.DIMENSION_ID)
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) player, Reference.DIMENSION_ID, new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID)));
        }
        return true;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
