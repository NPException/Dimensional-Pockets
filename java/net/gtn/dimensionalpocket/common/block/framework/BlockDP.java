package net.gtn.dimensionalpocket.common.block.framework;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.IBlockInteract;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockDP extends Block {

    public BlockDP(Material material, String name) {
        super(material);
        setBlockName(name);
        setCreativeTab(DimensionalPockets.creativeTab);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockInteract)
            ((IBlockInteract) tileEntity).onBlockActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockPlaced(entityLiving, itemStack);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        onBlockRemoval(world, x, y, z);
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        onBlockRemoval(world, x, y, z);
        return super.removedByPlayer(world, player, x, y, z);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockDestroyed();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onNeighbourBlockChanged(world, x, y, z, block);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + getUnlocalizedName().replace("tile.", ""));
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return side != -1;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return !renderWithModel();
    }

    @Override
    public boolean isOpaqueCube() {
        return !renderWithModel();
    }

    @Override
    public int getRenderType() {
        return renderWithModel() ? -1 : super.getRenderType();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getTileEntity(metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return getTileEntity(metadata) != null;
    }

    public boolean renderWithModel() {
        return false;
    }

    public abstract TileEntity getTileEntity(int metadata);
}
