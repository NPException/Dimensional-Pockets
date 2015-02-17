package net.gtn.dimensionalpocket.common.tileentity;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.ref.WeakReference;

public class TileDimensionalPocketWallConnector extends TileDP implements IBlockNotifier, IBlockInteract,
                                                                    IEnergyReceiver, IEnergyProvider, IEnergyHandler,
                                                                    ISidedInventory {
    
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
        	    ForgeDirection wallSide = Pocket.getSideForConnector(getCoordSet().toChunkOffset());
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
	    CoordSet offSet = getCoordSet().asChunkOffset();
	    
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

    @Override
    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        return false;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        // do nothing
    }

    @Override
    public void onBlockAdded(EntityLivingBase entityLivingBase, World world, int x, int y, int z, ItemStack itemStack) {
        // do nothing
    }

    @Override
    public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block) {
        if (world.isRemote) return;
        
        Pocket p = getPocket();
        if (p != null)
            p.markSourceBlockForUpdate();
    }

    @Override
    public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        if (!(world instanceof World) || ((World)world).isRemote) return;
        
        Pocket p = getPocket();
        if (p != null)
            p.markSourceBlockForUpdate();
    }
    
    // /////////////////////
    // INVENTORY METHODS //
    // /////////////////////

    private IInventory getInventoryOnOutsideWall() {
        Pocket p = getPocket();
        if (p == null)
            return null;

        ForgeDirection fdSide = Pocket.getSideForConnector(getCoordSet().asChunkOffset());

        switch (p.getFlowState(fdSide)) {
            case ENERGY:
                TileEntity te = getDimPocketNeighbourTileEntity(fdSide);
                if (te instanceof IInventory) {
                    return Utils.getInventory((IInventory) te);
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public int getSizeInventory() {
        return 6; // just doing the same as Compact Machines here.
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return null;
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return null;
        return inventory.decrStackSize(slot, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory != null)
            inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return 0;

        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {
        // do nothing
    }

    @Override
    public void closeInventory() {
        // do nothing
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return false;
        
        return inventory.isItemValidForSlot(slot, stack);
    }

    // Sided inventory //
    
    private static final int[] EMPTY_SLOT_ARRAY = new int[0];
    // cache for inventory slot arrays to prevent frequent allocation of arrays.
    // REMOVE the caching IF it turns out something is modifying the cached arrays.
    // Using weak references so the GC can still recycle TEs that are only referenced here. (after their chunk unloaded f.e.)
    private transient WeakReference<IInventory> lastKnownInventory;
    private transient int[] lastKnownInventorySlots;

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side>5)
            return EMPTY_SLOT_ARRAY;
        
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return EMPTY_SLOT_ARRAY;
        
        if (inventory instanceof ISidedInventory)
            return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side);
        
        // use cache for plain IInventory if possible        
        if (lastKnownInventory != null && lastKnownInventory.get() == inventory
                && inventory.getSizeInventory() == lastKnownInventorySlots.length) {
            return lastKnownInventorySlots;
        }
        
        int[] slots = new int[inventory.getSizeInventory()];
        for (int i=0; i < slots.length; i++) {
            slots[i] = i;
        }
        
        // store in cache
        lastKnownInventory = new WeakReference<>(inventory);
        lastKnownInventorySlots = slots;
        
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return false;
        
        return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canInsertItem(slot, stack, side);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        IInventory inventory = getInventoryOnOutsideWall();
        if (inventory == null)
            return false;
        
        return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canExtractItem(slot, stack, side);
    }
}
