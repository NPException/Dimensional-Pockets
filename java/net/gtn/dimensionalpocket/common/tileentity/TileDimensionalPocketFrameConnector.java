package net.gtn.dimensionalpocket.common.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.interfaces.IBlockInteract;
import net.gtn.dimensionalpocket.common.core.interfaces.IBlockNotifier;
import net.gtn.dimensionalpocket.common.core.pocket.FlowState;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDimensionalPocketFrameConnector extends TileDP implements IEnergyReceiver, IEnergyProvider {

    private Pocket pocket;
    
    public Pocket getPocket() {
    	System.out.println("Is client side: " + getWorldObj().isRemote);
        if (pocket == null)
            pocket = PocketRegistry.getPocket(getCoordSet().asChunkCoords());
        if (pocket == null)
        	DPLogger.warning("Did not find a Pocket for DPFrameConnector at " + getCoordSet().toString(), getClass());
        return pocket;
    }
    
    private int counter = 0;
    
    /**
     * Checks periodically if this DPFrameConnector is the valid one for the pocket.
     * If not, it will be invalidated.
     */
    @Override
    public void updateEntity() {
    	if (worldObj.isRemote) return;
    	
    	if (counter++ > 10) {
    		counter = 0;
    		
    		Pocket p = getPocket();
        	if (p != null
        			&& !getCoordSet().equals(
        					p.getConnectorCoords(
        						p.getSideForBlock(
        							getCoordSet().asChunkOffset()
        						)
        					)
        				)) {
        		invalidate();
        	}
    	}
    }

    /**
     * Checks if this DPFrameConnector can connect to energy networks
     */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		Pocket p = getPocket();
		if (p == null) return false;
		
		switch (p.getFlowState(from)) {
			case ENERGY_INPUT:
			case ENERGY_OUTPUT:
				return true;
			default:
				return false;
		}
	}
	
	/** 
	 * Returns the neighboring TileEntity of the DimensionalPocket block in the given direction.
	 * 
	 * @param direction
	 * @return the neighboring TE, or null if the the chunk is not loaded or no TE exists at the spot.
	 */
	private TileEntity getDimPocketNeighbourTileEntity(ForgeDirection direction) {
		Pocket p = getPocket();
		if (p == null) return null;
		
		World targetWorld = p.getBlockWorld();
		
		CoordSet targetCoords = p.getBlockCoords();
		if (! targetWorld.blockExists(targetCoords.getX(), targetCoords.getY(), targetCoords.getZ())) {
			return null;
		}
		
		targetCoords.addForgeDirection(direction);
		if (! targetWorld.blockExists(targetCoords.getX(), targetCoords.getY(), targetCoords.getZ())) {
			return null;
		}
		
		return targetWorld.getTileEntity(targetCoords.getX(), targetCoords.getY(), targetCoords.getZ());
	}

	/**
	 * Redirects the receiveEnergy call to the neighboring TileEntity of the DimensionalPocket block if possible.
	 */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());
		
		if (targetTE instanceof IEnergyReceiver) {
			int received = ((IEnergyReceiver) targetTE).receiveEnergy(from, maxReceive, simulate);
			System.out.println("DPConnector - receiveEnergy - " + targetTE.getBlockType().getUnlocalizedName() + " - sim: " + simulate + " - received: " + received); // TODO: REMOVE THIS LINE AFTER TESTING
			return received;
		}
		
		System.out.println("DPConnector - receiveEnergy - no IEnergyReceiver connected on side " + from.getOpposite().name()); // TODO: REMOVE THIS LINE AFTER TESTING
		return 0;
	}
	
	/**
	 * Redirects the extractEnergy call to the neighboring TileEntity of the DimensionalPocket block if possible.
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());
		
		if (targetTE instanceof IEnergyProvider) {
			int extracted = ((IEnergyProvider) targetTE).extractEnergy(from, maxExtract, simulate);
			System.out.println("DPConnector - extractEnergy - " + targetTE.getBlockType().getUnlocalizedName() + " - sim: " + simulate + " - extracted: " + extracted); // TODO: REMOVE THIS LINE AFTER TESTING
			return extracted;
		}
		
		System.out.println("DPConnector - extractEnergy - no IEnergyProvider connected on side " + from.getOpposite().name()); // TODO: REMOVE THIS LINE AFTER TESTING
		return 0;
	}

	/**
	 * Redirects the getEnergyStored call to the neighboring TileEntity of the DimensionalPocket block if possible.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());
		
		if (targetTE instanceof IEnergyProvider) {
			int stored = ((IEnergyProvider) targetTE).getEnergyStored(from);
			System.out.println("DPConnector - getEnergyStored - " + targetTE.getBlockType().getUnlocalizedName() + " - stored: " + stored); // TODO: REMOVE THIS LINE AFTER TESTING
			return stored;
		} else if (targetTE instanceof IEnergyReceiver) {
			int stored = ((IEnergyReceiver) targetTE).getEnergyStored(from);
			System.out.println("DPConnector - getEnergyStored - " + targetTE.getBlockType().getUnlocalizedName() + " - stored: " + stored); // TODO: REMOVE THIS LINE AFTER TESTING
			return stored;
		}
		
		System.out.println("DPConnector - getEnergyStored - no IEnergyProvider or IEnergyReceiver connected on side " + from.getOpposite().name()); // TODO: REMOVE THIS LINE AFTER TESTING
		return 0;
	}

	/**
	 * Redirects the getMaxEnergyStored call to the neighboring TileEntity of the DimensionalPocket block if possible.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());
		
		if (targetTE instanceof IEnergyProvider) {
			int maxStored = ((IEnergyProvider) targetTE).getEnergyStored(from);
			System.out.println("DPConnector - getMaxEnergyStored - " + targetTE.getBlockType().getUnlocalizedName() + " - maxStored: " + maxStored); // TODO: REMOVE THIS LINE AFTER TESTING
			return maxStored;
		} else if (targetTE instanceof IEnergyReceiver) {
			int maxStored = ((IEnergyReceiver) targetTE).getEnergyStored(from);
			System.out.println("DPConnector - getMaxEnergyStored - " + targetTE.getBlockType().getUnlocalizedName() + " - maxStored: " + maxStored); // TODO: REMOVE THIS LINE AFTER TESTING
			return maxStored;
		}
		
		System.out.println("DPConnector - getMaxEnergyStored - no IEnergyProvider or IEnergyReceiver connected on side " + from.getOpposite().name()); // TODO: REMOVE THIS LINE AFTER TESTING
		return 0;
	}
}
