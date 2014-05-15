package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.gtn.dimensionalpocket.common.core.IBlockNotifier;
import net.gtn.dimensionalpocket.common.core.PocketDimensionHelper;
import net.gtn.dimensionalpocket.common.core.TeleportingRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    CoordSet chunkSet = new CoordSet(0, 0, 0);
    boolean hasChunkSet = false;

    @Override
    public void onBlockPlaced() {

    }

    @Override
    public void onBlockDestroyed() {
        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        chunkSet.writeToNBT(itemStack.getTagCompound());

        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, itemStack));
    }

    public void genChunkSet() {
        if (hasChunkSet)
            return;
        chunkSet = TeleportingRegistry.genNewChunkSet();
        hasChunkSet = true;
    }

    public boolean hasChunkSet() {
        return hasChunkSet;
    }

    public void setChunkSet(CoordSet chunkSet) {
        this.chunkSet = chunkSet;
    }

}
