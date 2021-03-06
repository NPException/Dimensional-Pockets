package net.gtn.dimensionalpocket.common.tileentity;

import static net.gtn.dimensionalpocket.common.core.utils.DPAnalytics.analytics;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.cdmp.api.wormhole.IWormhole;
import de.cdmp.api.wormhole.WormholeTarget;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.SidedEnvironment;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketWall;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.oc.common.interfaces.IBlockInteract;
import net.gtn.dimensionalpocket.oc.common.interfaces.IBlockNotifier;
import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;


@Optional.Interface(iface = "li.cil.oc.api.network.SidedEnvironment", modid = "OpenComputers")
public class TileDimensionalPocketWallConnector extends TileDP
implements IBlockNotifier, IBlockInteract, IEnergyHandler, IFluidHandler, ISidedInventory, IWormhole, SidedEnvironment {

	// start of analytics variables //
	private int analyticTicksPassed = 0;

	private long rfTransferedIn = 0l;
	private long rfTransferedOut = 0l;
	private long fluidsTransferedIn = 0l;
	private long fluidsTransferedOut = 0l;
	// end of analytics variables //

	private void sendTileAnalytics() {
		// RF going into the pocket
		if (rfTransferedIn > 0) {
			long amount = rfTransferedIn;
			rfTransferedIn = 0;

			amount -= Integer.MAX_VALUE;
			while (amount > 0) {
				analytics.logRFTransferIn(Integer.MAX_VALUE);
				amount -= Integer.MAX_VALUE;
			}
			if (amount < 0) {
				analytics.logRFTransferIn((int) (amount + Integer.MAX_VALUE));
			}
		}

		// RF coming out of the pocket
		if (rfTransferedOut > 0) {
			long amount = rfTransferedOut;
			rfTransferedOut = 0;

			amount -= Integer.MAX_VALUE;
			while (amount > 0) {
				analytics.logRFTransferOut(Integer.MAX_VALUE);
				amount -= Integer.MAX_VALUE;
			}
			if (amount < 0) {
				analytics.logRFTransferOut((int) (amount + Integer.MAX_VALUE));
			}
		}

		// fluids going into the pocket
		if (fluidsTransferedIn > 0) {
			long amount = fluidsTransferedIn;
			fluidsTransferedIn = 0;

			amount -= Integer.MAX_VALUE;
			while (amount > 0) {
				analytics.logFluidTransferIn(Integer.MAX_VALUE);
				amount -= Integer.MAX_VALUE;
			}
			if (amount < 0) {
				analytics.logFluidTransferIn((int) (amount + Integer.MAX_VALUE));
			}
		}

		// fluids coming out of the pocket
		if (fluidsTransferedOut > 0) {
			long amount = fluidsTransferedOut;
			fluidsTransferedOut = 0;

			amount -= Integer.MAX_VALUE;
			while (amount > 0) {
				analytics.logFluidTransferOut(Integer.MAX_VALUE);
				amount -= Integer.MAX_VALUE;
			}
			if (amount < 0) {
				analytics.logFluidTransferOut((int) (amount + Integer.MAX_VALUE));
			}
		}
	}

	boolean newTile = true;

	@SideOnly(Side.CLIENT)
	private Pocket pocket;

	@Override
	public Pocket getPocket() {
		if (worldObj.isRemote) {
			return pocket;
		}

		return PocketRegistry.getPocket(getCoordSet().asChunkCoords());
	}

	private int counter = 0;

	/**
	 * Checks periodically if this DPWallConnector is the valid one for the
	 * pocket. If not, it will be invalidated.
	 */
	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}

		if (newTile) {
			newTile = false;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, BlockDimensionalPocketWall.CONNECTOR_META, 3);
		}

		if (++counter > 20) {
			counter = 0;

			Pocket p = getPocket();
			if (p != null) {
				CoordSet tileCoords = getCoordSet();
				ForgeDirection wallSide = Pocket.getSideForConnector(tileCoords.toChunkOffset());
				CoordSet connectorCoords = p.getConnectorCoords(wallSide);
				if (!tileCoords.equals(connectorCoords)) {
					DPLogger.debug("Connector:" + wallSide.name() + ":" + tileCoords.toString() + " invalid -> current Connector=" + String.valueOf(connectorCoords));
					invalidateConnector();
				}
			}
		}

		// add analytics events every 100ish ticks
		analyticTicksPassed++;
		if (analyticTicksPassed > 100) {
			analyticTicksPassed = 0;
			if (analytics.isActive()) {
				sendTileAnalytics();
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
		Pocket pocket = getPocket();
		if (pocket != null) {
			pocket.writeToNBT(tag);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		if (worldObj != null && worldObj.isRemote) {
			pocket = Pocket.readFromNBT(tag);
		}
	}

	/**
	 * Checks if this DPFrameConnector can connect to energy networks
	 */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		Pocket p = getPocket();
		if (p == null) {
			return false;
		}

		switch (p.getFlowState(from.getOpposite())) {
			case ENERGY:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Returns the neighboring TileEntity of the DimensionalPocket block in the
	 * given direction.
	 *
	 * @param direction
	 * @return the neighboring TE, or null if the the chunk is not loaded or no
	 *         TE exists at the spot.
	 */
	private TileEntity getDimPocketNeighbourTileEntity(ForgeDirection direction) {
		if (worldObj.isRemote) {
			return null;
		}

		Pocket p = getPocket();
		if (p == null) {
			return null;
		}

		World targetWorld = p.getBlockWorld();
		CoordSet targetCoords = p.getBlockCoords();

		// check if a DP is placed
		if (!targetWorld.blockExists(targetCoords.x, targetCoords.y, targetCoords.z)) {
			return null;
		}

		// check the neigbouring TE
		targetCoords.addForgeDirection(direction);
		return targetWorld.blockExists(targetCoords.x, targetCoords.y, targetCoords.z) ? targetWorld.getTileEntity(targetCoords.x, targetCoords.y, targetCoords.z) : null;
	}

	/**
	 * Redirects the receiveEnergy call to the neighboring TileEntity of the
	 * DimensionalPocket block if possible.
	 */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IEnergyReceiver) {
			int received = ((IEnergyReceiver) targetTE).receiveEnergy(from, maxReceive, simulate);
			if (!simulate && !worldObj.isRemote) {
				rfTransferedOut += received;
			}
			return received;
		}

		return 0;
	}

	/**
	 * Redirects the extractEnergy call to the neighboring TileEntity of the
	 * DimensionalPocket block if possible.
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IEnergyProvider) {
			int extracted = ((IEnergyProvider) targetTE).extractEnergy(from, maxExtract, simulate);
			if (!simulate && !worldObj.isRemote) {
				rfTransferedIn += extracted;
			}
			return extracted;
		}

		return 0;
	}

	/**
	 * Redirects the getEnergyStored call to the neighboring TileEntity of the
	 * DimensionalPocket block if possible.
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
	 * Redirects the getMaxEnergyStored call to the neighboring TileEntity of the
	 * DimensionalPocket block if possible.
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

		boolean ignoreX = offSet.x == 0 || offSet.x == 15;
		boolean ignoreY = offSet.y == 0 || offSet.y == 15;
		boolean ignoreZ = offSet.z == 0 || offSet.z == 15;

		int ox = offSet.x - 1;
		int oy = offSet.y - 1;
		int oz = offSet.z - 1;

		return AxisAlignedBB.getBoundingBox(
				xCoord - (ignoreX ? 0 : ox),
				yCoord - (ignoreY ? 0 : oy),
				zCoord - (ignoreZ ? 0 : oz),
				xCoord + (ignoreX ? 1 : 14 - ox),
				yCoord + (ignoreY ? 1 : 14 - oy),
				zCoord + (ignoreZ ? 1 : 14 - oz)
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
		if (world.isRemote) {
			return;
		}

		Pocket p = getPocket();
		if (p != null) {
			p.markSourceBlockForUpdate();
		}
	}

	@Override
	public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		if (!(world instanceof World) || ((World) world).isRemote) {
			return;
		}

		Pocket p = getPocket();
		if (p != null) {
			p.markSourceBlockForUpdate();
		}
	}

	// /////////////////////
	// INVENTORY METHODS //
	// /////////////////////

	private IInventory getInventoryOnOutsideWall() {
		if (worldObj.isRemote) {
			return null;
		}

		Pocket p = getPocket();
		if (p == null) {
			return null;
		}

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
		if (inventory == null) {
			return null;
		}
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory == null) {
			return null;
		}
		return inventory.decrStackSize(slot, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory != null) {
			inventory.setInventorySlotContents(slot, stack);
		}
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean isCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory == null) {
			return 0;
		}

		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openChest() {
		// do nothing
	}

	@Override
	public void closeChest() {
		// do nothing
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory == null) {
			return false;
		}

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
	public int[] getSlotsForFace(int side) {
		if (side > 5) {
			return EMPTY_SLOT_ARRAY;
		}

		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory == null) {
			return EMPTY_SLOT_ARRAY;
		}

		if (inventory instanceof ISidedInventory) {
			return ((ISidedInventory) inventory).getSlotsForFace(side);
		}

		// use cache for plain IInventory if possible
		if (lastKnownInventory != null && lastKnownInventory.get() == inventory
				&& inventory.getSizeInventory() == lastKnownInventorySlots.length) {
			return lastKnownInventorySlots;
		}

		int[] slots = new int[inventory.getSizeInventory()];
		for (int i = 0; i < slots.length; i++) {
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
		if (inventory == null) {
			return false;
		}

		return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canInsertItem(slot, stack, side);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		IInventory inventory = getInventoryOnOutsideWall();
		if (inventory == null) {
			return false;
		}

		return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canExtractItem(slot, stack, side);
	}

	// ////////////////
	// WORMHOLE API //
	// ////////////////

	@Override
	public List<WormholeTarget<Block, TileEntity>> getAllTargets(ForgeDirection fromDirection) {
		if (worldObj.isRemote) {
			return Collections.emptyList();
		}

		Pocket p = getPocket();
		if (p == null) {
			return Collections.emptyList();
		}

		World targetWorld = p.getBlockWorld();
		CoordSet targetCoords = p.getBlockCoords();

		// check if a DP is placed
		if (!targetWorld.blockExists(targetCoords.x, targetCoords.y, targetCoords.z)) {
			return Collections.emptyList();
		}

		ForgeDirection direction = fromDirection.getOpposite();
		targetCoords.addForgeDirection(direction);

		List<WormholeTarget<Block, TileEntity>> wormholeTargets = new ArrayList<>(1);
		wormholeTargets.add(new WormholeTarget<>(targetWorld, targetCoords.x, targetCoords.y, targetCoords.z, direction));

		return wormholeTargets;
	}

	///////////////////
	// OpenComputers //
	///////////////////

	@Optional.Method(modid = "OpenComputers")
	@Override
	public Node sidedNode(ForgeDirection side) {
		if (worldObj.isRemote) {
			return null;
		}

		Pocket p = getPocket();
		if (p == null) {
			return null;
		}

		side = side.getOpposite();

		switch (p.getFlowState(side)) {
			case ENERGY:
				TileEntity te = getDimPocketNeighbourTileEntity(side);
				if (te instanceof SidedEnvironment) {
					return ((SidedEnvironment) te).sidedNode(side);
				} else if (te instanceof Environment) {
					return ((Environment) te).node();
				}
				return null;
			default:
				return null;
		}
	}

	@Optional.Method(modid = "OpenComputers")
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canConnect(ForgeDirection side) {
		return true;
	}

	////////////////////
	// Fluid Handling //
	////////////////////

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			int filled = ((IFluidHandler) targetTE).fill(from, resource, doFill);
			if (doFill && !worldObj.isRemote) {
				fluidsTransferedOut += filled;
			}
			return filled;
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			FluidStack drained = ((IFluidHandler) targetTE).drain(from, resource, doDrain);
			if (drained != null && doDrain && !worldObj.isRemote) {
				fluidsTransferedIn += drained.amount;
			}
			return drained;
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			FluidStack drained = ((IFluidHandler) targetTE).drain(from, maxDrain, doDrain);
			if (drained != null && doDrain && !worldObj.isRemote) {
				fluidsTransferedIn += drained.amount;
			}
			return drained;
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			return ((IFluidHandler) targetTE).canFill(from, fluid);
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			return ((IFluidHandler) targetTE).canDrain(from, fluid);
		}
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		TileEntity targetTE = getDimPocketNeighbourTileEntity(from.getOpposite());

		if (targetTE instanceof IFluidHandler) {
			return ((IFluidHandler) targetTE).getTankInfo(from);
		}
		return null;
	}
}
