package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private Pocket pocket;

    private int ticksSinceLastLightCheck = 0;

    private PocketTeleportPreparation telePrep;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (telePrep != null) {
                if (telePrep.doPrepareTick()) {
                    telePrep = null;
                }
            }
            if (ticksSinceLastLightCheck++ > 200) {
                ticksSinceLastLightCheck = 0;
                getPocket().setLightLevel(fetchLightLevel());
            }
        }
    }

    private int fetchLightLevel() {
        float highestLevel = 0f;

        float level = worldObj.getLightBrightness(xCoord + 1, yCoord, zCoord);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        level = worldObj.getLightBrightness(xCoord - 1, yCoord, zCoord);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        level = worldObj.getLightBrightness(xCoord, yCoord + 1, zCoord);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        level = worldObj.getLightBrightness(xCoord, yCoord - 1, zCoord);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        level = worldObj.getLightBrightness(xCoord, yCoord, zCoord + 1);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        level = worldObj.getLightBrightness(xCoord, yCoord, zCoord - 1);
        highestLevel = (level > highestLevel) ? level : highestLevel;

        return Math.round(highestLevel * 15f);
    }

    @Override
    public void onBlockPlaced() {
    }

    @Override
    public void onBlockDestroyed() {
        if (worldObj.isRemote)
            return;

        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        getPocket().getChunkCoords().writeToNBT(itemStack.getTagCompound());

        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, itemStack);
        entityItem.delayBeforeCanPickup = 0;

        worldObj.spawnEntityInWorld(entityItem);
    }

    public Pocket getPocket() {
        if (pocket == null) {
            pocket = PocketRegistry.getOrCreatePocket(worldObj.provider.dimensionId, getCoordSet(), fetchLightLevel());
        }
        return pocket;
    }

    public boolean setPocket(CoordSet chunkSet) {
        Pocket newPocket = PocketRegistry.getPocket(chunkSet);

        if (newPocket != null)
            pocket = newPocket;

        return newPocket != null && newPocket.getChunkCoords().equals(chunkSet);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        getPocket().getChunkCoords().writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        CoordSet tempSet = CoordSet.readFromNBT(tag);

        pocket = PocketRegistry.getPocket(tempSet);
    }

    public void prepareTeleportIntoPocket(EntityPlayer player) {
        int ticksToTake = 15;
        if (!worldObj.isRemote) {
            // create a teleport preparation, which will then tick for the given amount of time, and then teleport the player
            telePrep = new PocketTeleportPreparation(player, ticksToTake, getPocket(), Direction.INTO_POCKET);
        } else {
            // TODO Sync for all clients.
            for (int i = 0; i < 40; i++)
                UtilsFX.createPlayerStream(player, getCoordSet(), ticksToTake);
        }
    }
}
