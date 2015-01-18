package net.gtn.dimensionalpocket.common.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.interfaces.IBlockInteract;
import net.gtn.dimensionalpocket.common.core.interfaces.IBlockNotifier;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDimensionalPocket extends TileDP implements IBlockNotifier, IBlockInteract, IEnergyReceiver, IEnergyProvider {

    private static final String TAG_CUSTOM_DP_NAME = "customDPName";

    private Pocket pocket;
    private String customName;

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

        Pocket pocket = getPocket();
        pocket.generatePocketRoom();

        ChunkLoaderHandler.addPocketToChunkLoader(pocket);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null || player.getCurrentEquippedItem() != null)
            return true;

        prepareTeleportIntoPocket(player);
        player.swingItem();
        return true;
    }

    @Override
    public void onBlockDestroyed() {
        if (worldObj.isRemote)
            return;

        Utils.spawnItemStack(generateItemStack(), worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
        unloadPocket();
    }

    public ItemStack generateItemStack() {
        ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        CoordSet chunkSet = getPocket().getChunkCoords();
        chunkSet.writeToNBT(itemStack.getTagCompound());

        int id = chunkSet.getX() * 16 + chunkSet.getY() + 1;

        itemStack = Utils.generateItem(itemStack, customName, false, "~ Pocket " + id + " ~");
        return itemStack;
    }

    public void unloadPocket() {
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

    @Override
    public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block) {
        // nothing to do
    }

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		// TODO read out info from pocket
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		// TODO Auto-generated method stub
		System.out.println("IEnergyReceiver - receiveEnergy - from: " + from.name() + " - maxReceive: " + maxReceive + " - simulate: " + simulate);
		return 0;
	}
	
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		// TODO Auto-generated method stub
		System.out.println("IEnergyProvider - extractEnergy - from: " + from.name() + " - maxExtract: " + maxExtract + " - simulate: " + simulate);
		return Math.min(5, maxExtract);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		System.out.println("IEnergyReceiver/Provider - getEnergyStored - from: " + from.name());
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		System.out.println("IEnergyReceiver/Provider - getMaxEnergyStored - from: " + from.name());
		return 0;
	}
}
