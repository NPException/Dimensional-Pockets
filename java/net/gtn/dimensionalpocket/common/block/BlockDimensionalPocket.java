package net.gtn.dimensionalpocket.common.block;

import cpw.mods.fml.client.ExtendedServerListData;
import net.gtn.dimensionalpocket.common.core.DPLogger;
import net.gtn.dimensionalpocket.common.items.ItemBlockHolder;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDimensionalPocket extends BlockDPMeta {

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (!world.isRemote) {
            player.travelToDimension(Reference.DIMENSION_ID);
            // MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        }
        // player.travelToDimension(Reference.DIMENSION_ID);
        // DPLogger.info(world.provider.dimensionId);
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

    @Override
    public String[] getNames() {
        return null;
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockHolder.class;
    }

}
