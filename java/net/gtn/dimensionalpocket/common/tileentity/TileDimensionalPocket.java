package net.gtn.dimensionalpocket.common.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private Map<ForgeDirection, Integer> strengthMap = new HashMap<ForgeDirection, Integer>();
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
            if (ticksSinceLastLightCheck++ > 20000) {
                ticksSinceLastLightCheck = 0;
                if (hasPocket())
                    pocket.setLightLevel((int) (worldObj.getLightBrightness(xCoord, yCoord, zCoord) * 15));
            }
        }
    }

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
        int initialLightLevel = (worldObj.getFullBlockLightValue(xCoord, yCoord, zCoord));
        pocket = PocketRegistry.generateNewPocket(worldObj.provider.dimensionId, getCoordSet(),initialLightLevel);
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
        return strengthMap.get(direction).intValue();
    }

    public void prepareTeleportIntoPocket(EntityPlayer player) {
        int ticksToTake = 15;
        if (!worldObj.isRemote) {
            if (!hasPocket())
                generateNewPocket();
            // create a teleport preparation, which will then tick for the given amount of time, and then teleport the player
            telePrep = new PocketTeleportPreparation(player, ticksToTake, getPocket(), Direction.INTO_POCKET);
        } else {
            // TODO Sync for all clients.
            for (int i = 0; i < 40; i++)
                UtilsFX.createPlayerStream(player, getCoordSet(), ticksToTake);
        }
    }
}
