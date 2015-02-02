package net.gtn.dimensionalpocket.common.tileentity;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileDimensionalPocketWallConnector extends TileDP implements IEnergyReceiver, IEnergyProvider, IEnergyHandler {
    
    boolean newTile = true;

    @SideOnly(Side.CLIENT)
    private Pocket pocket;
    
    @Override
    public Pocket getPocket() {
    	if (worldObj.isRemote)
    	    return pocket;

    	return PocketRegistry.getPocket(getCoordSet().asChunkCoords());
    }
    
    private int counter = 0;
    
    /**
     * Checks periodically if this DPWallConnector is the valid one for the pocket.
     * If not, it will be invalidated.
     */
    @Override
    public void updateEntity() {
    	if (worldObj.isRemote) return;
    	
    	if (newTile) {
    	    newTile = false;
    	    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, BlockDimensionalPocketWall.CONNECTOR_META, 3);
    	}
    	
    	if (++counter > 20) {
    		counter = 0;
    		
    		Pocket p = getPocket();
        	if (p != null) {
        	    ForgeDirection wallSide = Pocket.getSideForBlock(Utils.getOffsetInChunk(getCoordSet()));
        	    CoordSet connectorCoords = p.getConnectorCoords(wallSide);
        	    if (!getCoordSet().equals(connectorCoords)) {
        	        DPLogger.debug("Connector:"+ wallSide.name() + ":" + getCoordSet().toString() + " invalid -> current Connector=" + String.valueOf(connectorCoords));
        	        invalidateConnector();
        	    }
        	}
    	}
    }
    
    public void invalidateConnector() {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
        invalidate();
        markForUpdate();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        getPocket().writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        
        if (worldObj != null && worldObj.isRemote) // worldObj is null on initial world loading
            pocket = Pocket.readFromNBT(tag);
    }

    /**
     * Checks if this DPFrameConnector can connect to energy networks
     */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		Pocket p = getPocket();
		if (p == null) return false;
		
		switch (p.getFlowState(from.getOpposite())) {
			case ENERGY:
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
			return received;
		}
		
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
			return extracted;
		}
		
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
			return stored;
		} else if (targetTE instanceof IEnergyReceiver) {
			int stored = ((IEnergyReceiver) targetTE).getEnergyStored(from);
			return stored;
		}
		
		return 0;
	}

	/**
	 * Redirects the getMaxEnergyStored call to the neighboring TileEntity of the DimensionalPocket block if possible.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());
		
		if (targetTE instanceof IEnergyProvider) {
			int maxStored = ((IEnergyProvider) targetTE).getMaxEnergyStored(from);
			return maxStored;
		} else if (targetTE instanceof IEnergyReceiver) {
			int maxStored = ((IEnergyReceiver) targetTE).getMaxEnergyStored(from);
			return maxStored;
		}
		
		return 0;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
	    CoordSet offSet = Utils.getOffsetInChunk(getCoordSet());
	    
	    boolean ignoreX = offSet.getX() == 0 || offSet.getX() == 15;
	    boolean ignoreY = offSet.getY() == 0 || offSet.getY() == 15;
	    boolean ignoreZ = offSet.getZ() == 0 || offSet.getZ() == 15;
	    
	    int ox = offSet.getX()-1;
	    int oy = offSet.getY()-1;
	    int oz = offSet.getZ()-1;
	    
	    return AxisAlignedBB.getBoundingBox(
	            xCoord - (ignoreX ? 0 : ox),
	            yCoord - (ignoreY ? 0 : oy),
	            zCoord - (ignoreZ ? 0 : oz),
	            xCoord + (ignoreX ? 1 : 14-ox),
	            yCoord + (ignoreY ? 1 : 14-oy),
	            zCoord + (ignoreZ ? 1 : 14-oz)
	            );
	}
}
