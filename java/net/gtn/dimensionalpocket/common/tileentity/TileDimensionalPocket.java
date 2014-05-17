package net.gtn.dimensionalpocket.common.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private Map<ForgeDirection, Integer> strengthMap = new HashMap<ForgeDirection, Integer>();
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
        pocket = PocketRegistry.generateNewPocket(worldObj.provider.dimensionId, getCoordSet());
    }

    public boolean hasPocket() {
        return pocket != null;
    }

    public Pocket getPocket() {
        return pocket;
    }

    public boolean setPocket(CoordSet chunkSet) {
        pocket = PocketRegistry.getPocket(chunkSet);
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
        pocket = PocketRegistry.getPocket(tempSet);
    }

    public int getStrength(ForgeDirection direction) {
        return strengthMap.get(direction);
    }

    @Override
    public void onNeighbourBlockChanged() {
        // TODO This searches around and updates the strengthMap
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            strengthMap.put(direction, worldObj.getIndirectPowerLevelTo(xCoord, yCoord, zCoord, direction.ordinal()));

        DPLogger.info("");
        for (Integer i : strengthMap.values())
            DPLogger.info(i);
    }
}
