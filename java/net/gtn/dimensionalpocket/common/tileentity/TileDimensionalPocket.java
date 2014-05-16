package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.teleport.Pocket;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private Pocket pocket;

    @Override
    public void onBlockPlaced() {
    }

    @Override
    public void onBlockDestroyed() {
        if (worldObj.isRemote)
            return;

        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (hasPocket()) {
            if (!itemStack.hasTagCompound())
                itemStack.setTagCompound(new NBTTagCompound());
            pocket.getChunkCoords().writeToNBT(itemStack.getTagCompound());
        }

        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, itemStack);
        entityItem.delayBeforeCanPickup = 0;

        worldObj.spawnEntityInWorld(entityItem);
    }

    public void generateNewPocket() {
        if (hasPocket())
            return;
        pocket = TeleportingRegistry.genNewTeleportLink(worldObj.provider.dimensionId, getCoordSet());
    }

    public boolean hasPocket() {
        return pocket != null;
    }

    public Pocket getPocket() {
        return pocket;
    }

    public boolean setPocket(CoordSet chunkSet) {
        pocket = TeleportingRegistry.getPocket(chunkSet);
        return pocket != null && pocket.getChunkCoords().equals(chunkSet);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (hasPocket())
            pocket.getChunkCoords().writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        CoordSet tempSet = CoordSet.readFromNBT(tag);
        pocket = TeleportingRegistry.getPocket(tempSet);
    }
}
