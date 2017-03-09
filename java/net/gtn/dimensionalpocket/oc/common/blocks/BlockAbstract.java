package net.gtn.dimensionalpocket.oc.common.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.common.interfaces.IBlockInteract;
import net.gtn.dimensionalpocket.oc.common.interfaces.IBlockNotifier;
import net.gtn.dimensionalpocket.oc.common.interfaces.ITileProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockAbstract extends Block {

    protected boolean textureReg = true;
    public final boolean isTileProvider = this instanceof ITileProvider;
    public final String modIdentifier;

    public BlockAbstract(Material material, String name) {
        super(material);
        modIdentifier = Loader.instance().activeModContainer().getModId() + ":";
        setName(name);
        register(name);
    }

    public BlockAbstract setName(String name) {
        setBlockName(name);
        setBlockTextureName(name);
        return this;
    }

    public BlockAbstract setTextureless() {
        this.textureReg = false;
        return this;
    }


    public BlockAbstract register(String name) {
        GameRegistry.registerBlock(this, name);
        return this;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockInteract)
            return ((IBlockInteract) tileEntity).onActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
        return false;
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int process) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity.receiveClientEvent(id, process);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockAdded(entityLiving, world, x, y, z, itemStack);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        onBlockRemoval(world, x, y, z);
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        onBlockRemoval(world, x, y, z);
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockRemoval(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onNeighbourBlockChanged(world, x, y, z, block);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onNeighbourTileChanged(world, x, y, z, tileX, tileY, tileZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        if (textureReg)
            blockIcon = iconRegister.registerIcon(modIdentifier + getTextureName());
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return !isTileProvider ? null : ((ITileProvider) this).createNewTileEntity(world, metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return isTileProvider;
    }
}