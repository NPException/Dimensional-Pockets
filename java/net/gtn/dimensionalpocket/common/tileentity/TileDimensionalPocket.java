package net.gtn.dimensionalpocket.common.tileentity;

import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.IBlockNotifier;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier {

    private static final String TAG_CUSTOM_DP_NAME = "customDPName";

    private Pocket pocket;
    private String customName;

    private int prevLightLevel = 0;

    private PocketTeleportPreparation telePrep;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && telePrep != null)
            if (telePrep.doPrepareTick())
                telePrep = null;
    }

    @Override
    public void onBlockPlaced(EntityLivingBase entityLiving, ItemStack itemStack) {
        if (worldObj.isRemote)
            return;

        if (itemStack.hasTagCompound()) {
            NBTTagCompound itemCompound = itemStack.getTagCompound();

            CoordSet chunkSet = CoordSet.readFromNBT(itemCompound);
            if (chunkSet != null) {
                boolean success = setPocket(chunkSet);

                if (!success)
                    throw new RuntimeException("YOU DESERVED THIS!");

                PocketRegistry.updatePocket(getPocket().getChunkCoords(), entityLiving.dimension, getCoordSet());
            }

            if (itemCompound.hasKey("display")) {
                String tempString = itemCompound.getCompoundTag("display").getString("Name");
                if (!tempString.isEmpty())
                    customName = tempString;
            }
        }

        getPocket().generatePocketRoom();

        ChunkLoaderHandler.addPocketToChunkLoader(getPocket());
    }

    public int getLightForPocket() {
        if (pocket == null || !pocket.isSourceBlockPlaced())
            return 0;

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

        int currentLightLevel = Math.round(highestLevel * 15f);

        if (prevLightLevel != currentLightLevel) {
            prevLightLevel = currentLightLevel;
            pocket.forcePocketUpdate();
        }

        return currentLightLevel;
    }

    @Override
    public void onBlockDestroyed() {
        if (worldObj.isRemote)
            return;

        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        CoordSet chunkSet = getPocket().getChunkCoords();
        chunkSet.writeToNBT(itemStack.getTagCompound());

        int id = chunkSet.getX() * 16 + chunkSet.getY();

        itemStack = Utils.generateItem(itemStack, customName, false, "~ Pocket " + id + " ~");

        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, itemStack);
        entityItem.delayBeforeCanPickup = 0;

        worldObj.spawnEntityInWorld(entityItem);

        ChunkLoaderHandler.removePocketFromChunkLoader(getPocket());
    }

    public Pocket getPocket() {
        if (pocket == null)
            pocket = PocketRegistry.getOrCreatePocket(worldObj.provider.dimensionId, getCoordSet());
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
        if (customName != null)
            tag.setString(TAG_CUSTOM_DP_NAME, customName);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        CoordSet tempSet = CoordSet.readFromNBT(tag);
        pocket = PocketRegistry.getPocket(tempSet);

        String tempString = tag.getString(TAG_CUSTOM_DP_NAME);
        if (!tempString.isEmpty())
            customName = tempString;
    }

    public void prepareTeleportIntoPocket(EntityPlayer player) {
        int ticksToTake = 15;
        if (!worldObj.isRemote) {
            telePrep = new PocketTeleportPreparation(player, ticksToTake, getPocket(), Direction.INTO_POCKET);
        } else {
            // TODO Sync for all clients.
            for (int i = 0; i < 40; i++)
                UtilsFX.createPlayerStream(player, getCoordSet(), ticksToTake);
        }
    }
}
