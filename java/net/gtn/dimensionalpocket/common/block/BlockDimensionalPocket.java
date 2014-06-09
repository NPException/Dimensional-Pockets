package net.gtn.dimensionalpocket.common.block;

import java.util.ArrayList;

import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.states.RedstoneStateHandler.RedstoneSideState;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
                // RETURN FALSE
                return true;

            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileDimensionalPocket) {
                // remove all of this.
                TileDimensionalPocket tile = (TileDimensionalPocket) tileEntity;
                Pocket pocket = tile.getPocket();

                int temp = pocket.getRedstoneState().getStrength(side, RedstoneSideState.INPUT);
                DPLogger.info(temp);
            }
            // RETURN FALSE
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
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDimensionalPocket)
            return ((TileDimensionalPocket) tileEntity).getPocket().getRedstoneState().getStrength(side, RedstoneSideState.OUTPUT);
        return 0;
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
    public TileEntity getTileEntity(int metadata) {
        return new TileDimensionalPocket();
    }
}
