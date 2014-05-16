package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private CoordSet chunkSet;

    @Override
    public void onBlockPlaced() {
    }

    @Override
    public void onBlockDestroyed() {
        if (worldObj.isRemote)
            return;

        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (hasChunkSet()) {
            if (!itemStack.hasTagCompound())
                itemStack.setTagCompound(new NBTTagCompound());
            chunkSet.writeToNBT(itemStack.getTagCompound());
        }

        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, itemStack);
        entityItem.delayBeforeCanPickup = 0;

        worldObj.spawnEntityInWorld(entityItem);
    }

    public void genChunkSet() {
        if (hasChunkSet())
            return;
        chunkSet = TeleportingRegistry.genNewTeleportLink(worldObj.provider.dimensionId, getCoordSet());
    }

    public boolean hasChunkSet() {
        return chunkSet != null;
    }

    public void setChunkSet(CoordSet chunkSet) {
        this.chunkSet = chunkSet;
    }

    public CoordSet getChunkSet() {
        return chunkSet;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (hasChunkSet())
            chunkSet.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        chunkSet = CoordSet.readFromNBT(tag);
    }
}
