package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.sidestates.ISideState;
import net.gtn.dimensionalpocket.common.core.sidestates.RedstoneState;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocket extends BlockDP {

    private IIcon[] icons = new IIcon[Reference.SIDE_STATE_COUNT];

    public BlockDimensionalPocket(Material material, String name) {
        super(material, name);
        setHardness(4F);
        setResistance(15F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return true;

        ItemStack itemStack = player.getCurrentEquippedItem();

        if (itemStack != null) {
            if (world.isRemote)
                return true;

            if (itemStack.getItem() == ModItems.miscItems && itemStack.getItemDamage() == 2) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileDimensionalPocket) {
                    TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
                    Pocket pocket = tile.getPocket();
                    pocket.setSideState(side, new RedstoneState());
                    DPLogger.info("SET SIDE STATE");
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                }
            }
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket) {
            TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
            tile.prepareTeleportIntoPocket(player);
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return side != -1;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket) {
            Pocket pocket = ((TileDimensionalPocket) tileEntity).getPocket();
            ISideState sideState = pocket.getSideState(side);

            if (!(sideState instanceof RedstoneState))
                return 0;

            RedstoneState redstoneState = (RedstoneState) sideState;

            return redstoneState.getOutputSignal(side);
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileDimensionalPocket();
    }
}
