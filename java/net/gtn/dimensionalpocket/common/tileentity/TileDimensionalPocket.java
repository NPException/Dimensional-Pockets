package net.gtn.dimensionalpocket.common.tileentity;

import static net.gtn.dimensionalpocket.common.core.utils.DPAnalytics.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.SidedEnvironment;
import me.jezza.oc.common.interfaces.IBlockInteract;
import me.jezza.oc.common.interfaces.IBlockNotifier;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.ChunkLoaderHandler;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation;
import net.gtn.dimensionalpocket.common.core.pocket.PocketTeleportPreparation.Direction;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.cdmp.api.wormhole.IWormhole;
import de.cdmp.api.wormhole.WormholeTarget;


@Optional.Interface(iface = "li.cil.oc.api.network.SidedEnvironment", modid = "OpenComputers")
public class TileDimensionalPocket extends TileDP
implements IBlockNotifier, IBlockInteract, IEnergyHandler, IFluidHandler, ISidedInventory, IWormhole, SidedEnvironment {

	// start of analytics variables //
	private int analyticTicksPassed = 0;
	private final ReentrantLock analyticsLock = new ReentrantLock(true);

	private long rfTransferedIn = 0l;
	private long rfTransferedOut = 0l;
	private long fluidsTransferedIn = 0l;
	private long fluidsTransferedOut = 0l;

	private boolean alreadyMarkedAsRemoved = false;
	// end of analytics variables //

	private void sendTileAnalytics() {
		try {
			analyticsLock.lock();

			// RF going into the pocket
			if (rfTransferedIn > 0) {
				rfTransferedIn = rfTransferedIn - Integer.MAX_VALUE;
				while (rfTransferedIn > 0) {
					analytics.logRFTransferIn(Integer.MAX_VALUE);
					rfTransferedIn = rfTransferedIn - Integer.MAX_VALUE;
				}
				if (rfTransferedIn < 0) {
					analytics.logRFTransferIn((int) (rfTransferedIn + Integer.MAX_VALUE));
				}
				rfTransferedIn = 0;
			}

			// RF coming out of the pocket
			if (rfTransferedOut > 0) {
				rfTransferedOut = rfTransferedOut - Integer.MAX_VALUE;
				while (rfTransferedOut > 0) {
					analytics.logRFTransferOut(Integer.MAX_VALUE);
					rfTransferedOut = rfTransferedOut - Integer.MAX_VALUE;
				}
				if (rfTransferedOut < 0) {
					analytics.logRFTransferOut((int) (rfTransferedOut + Integer.MAX_VALUE));
				}
				rfTransferedOut = 0;
			}

			// fluids going into the pocket
			if (fluidsTransferedIn > 0) {
				fluidsTransferedIn = fluidsTransferedIn - Integer.MAX_VALUE;
				while (fluidsTransferedIn > 0) {
					analytics.logFluidTransferIn(Integer.MAX_VALUE);
					fluidsTransferedIn = fluidsTransferedIn - Integer.MAX_VALUE;
				}
				if (fluidsTransferedIn < 0) {
					analytics.logFluidTransferIn((int) (fluidsTransferedIn + Integer.MAX_VALUE));
				}
				fluidsTransferedIn = 0;
			}

			// fluids coming out of the pocket
			if (fluidsTransferedOut > 0) {
				fluidsTransferedOut = fluidsTransferedOut - Integer.MAX_VALUE;
				while (fluidsTransferedOut > 0) {
					analytics.logFluidTransferOut(Integer.MAX_VALUE);
					fluidsTransferedOut = fluidsTransferedOut - Integer.MAX_VALUE;
				}
				if (fluidsTransferedOut < 0) {
					analytics.logFluidTransferOut((int) (fluidsTransferedOut + Integer.MAX_VALUE));
				}
				fluidsTransferedOut = 0;
			}
		} finally {
			analyticsLock.unlock();
		}
	}

	private static final String TAG_CUSTOM_DP_NAME = "customDPName";

	private String customName;

	@SideOnly(Side.CLIENT)
	private Pocket pocket;
	private PocketTeleportPreparation telePrep;

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;

		if (telePrep != null) {
			if (telePrep.doPrepareTick()) {
				telePrep = null;
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

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (worldObj.isRemote)
			return;

		Utils.spawnItemStack(generateItemStackOnRemoval(), worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 0);
	}

	@Override
	public void onNeighbourBlockChanged(World world, int x, int y, int z, Block block) {
		if (world.isRemote)
			return;

		Pocket p = getPocket();
		if (p != null) {
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				p.markConnectorForUpdate(side);
			}
		}
	}

	@Override
	public void onNeighbourTileChanged(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		if (!(world instanceof World) || ((World) world).isRemote)
			return;

		Pocket p = getPocket();
		if (p != null) {
			int ox = tileX - x;
			int oy = tileY - y;
			int oz = tileZ - z;

			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				if (side.offsetX == ox && side.offsetY == oy && side.offsetZ == oz) {
					p.markConnectorForUpdate(side);
				}
			}
		}
	}

	@Override
	public void onBlockAdded(EntityLivingBase entityLivingBase, World world, int x, int y, int z, ItemStack itemStack) {
		if (worldObj.isRemote)
			return;

		if (itemStack.hasTagCompound()) {
			NBTTagCompound itemCompound = itemStack.getTagCompound();

			CoordSet chunkSet = CoordSet.readFromNBT(itemCompound);
			boolean success = PocketRegistry.getPocket(chunkSet) != null;

			if (!success)
				throw new RuntimeException("YOU DESERVED THIS!");

			PocketRegistry.updatePocket(chunkSet, entityLivingBase.dimension, getCoordSet());

			if (itemCompound.hasKey("display")) {
				String tempString = itemCompound.getCompoundTag("display").getString("Name");
				if (!tempString.isEmpty()) {
					customName = tempString;
				}
			}

			if (entityLivingBase instanceof EntityPlayer) {
				EntityPlayerMP player = (EntityPlayerMP) entityLivingBase;
				if (player.getCurrentEquippedItem() == itemStack) {
					player.destroyCurrentEquippedItem();
				}
			}
		}

		Pocket pocket = getPocket();
		pocket.generatePocketRoom(entityLivingBase.getCommandSenderName());

		ChunkLoaderHandler.addPocketToChunkLoader(pocket);

		if (analytics.isActive()) {
			analytics.logPocketPlaced();
		}
	}

	@Override
	public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
		if (player == null)
			return true;

		ItemStack equippedItemStack = player.getCurrentEquippedItem();
		if (equippedItemStack != null) {
			if (Utils.isItemPocketWrench(equippedItemStack)) {
				ModItems.netherCrystal.onItemUseFirst(equippedItemStack, player, world, x, y, z, side, hitVecX, hitVecY, hitVecZ);
			}
			return true;
		}

		prepareTeleportIntoPocket(player);
		return true;
	}

	public ItemStack generateItemStackOnRemoval() {
		if (!alreadyMarkedAsRemoved && analytics.isActive()) {
			alreadyMarkedAsRemoved = true;
			analytics.logPocketMined();
		}

		ItemStack itemStack = new ItemStack(ModBlocks.dimensionalPocket);

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		CoordSet chunkSet = getPocket().getChunkCoords();
		chunkSet.writeToNBT(itemStack.getTagCompound());

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = "Creator: §3§o" + pocket.getCreator();
		}

		CoordSet blockSet = chunkSet.toBlockCoords();

		itemStack = Utils.generateItem(itemStack, customName, false, "~ Pocket §e" + blockSet.x + "," + blockSet.y + "," + blockSet.z + "§8 ~", creatorLore);
		return itemStack;
	}

	@Override
	public Pocket getPocket() {
		if (worldObj.isRemote)
			return pocket;

		return PocketRegistry.getOrCreatePocket(worldObj, getCoordSet());
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		getPocket().writeToNBT(tag);
		if (customName != null) {
			tag.setString(TAG_CUSTOM_DP_NAME, customName);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		if (worldObj != null && worldObj.isRemote) {
			pocket = Pocket.readFromNBT(tag);
		}

		String tempString = tag.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
	}

	public void prepareTeleportIntoPocket(EntityPlayer player) {
		int ticksToTake = 15;
		if (!worldObj.isRemote) {
			telePrep = new PocketTeleportPreparation(player, ticksToTake, getPocket(), Direction.INTO_POCKET);
		} else {
			// TODO Sync for all clients.
			for (int i = 0; i < 40; i++) {
				UtilsFX.createPlayerStream(player, getCoordSet(), ticksToTake);
			}
		}
	}

	/**
	 * Returns the neighboring TileEntity of the Frame connector block at the
	 * given side.
	 *
	 * @param side
	 * @return the neighboring TE, or null if the the chunk is not loaded or no
	 *         TE exists at the spot.
	 */
	private TileEntity getFrameConnectorNeighborTileEntity(ForgeDirection side) {
		if (worldObj.isRemote)
			return null;

		Pocket p = getPocket();
		if (p == null)
			return null;

		World targetWorld = MinecraftServer.getServer().worldServerForDimension(Reference.DIMENSION_ID);

		CoordSet targetCoords = p.getConnectorCoords(side);
		if (targetCoords == null || !targetWorld.blockExists(targetCoords.x, targetCoords.y, targetCoords.z))
			return null;

		targetCoords.addForgeDirection(side.getOpposite());
		return targetWorld.blockExists(targetCoords.x, targetCoords.y, targetCoords.z) ? targetWorld.getTileEntity(targetCoords.x, targetCoords.y, targetCoords.z) : null;

	}

	///////////////////////
	// RF ENERGY METHODS //
	///////////////////////

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		Pocket p = getPocket();
		if (p == null)
			return false;

		switch (p.getFlowState(from)) {
			case ENERGY:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Redirects the receiveEnergy call to the neighboring TileEntity of the
	 * corresponding FrameConnector wall block if possible.
	 */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IEnergyReceiver) {
			int received = ((IEnergyReceiver) targetTE).receiveEnergy(from, maxReceive, simulate);
			if (!simulate && !worldObj.isRemote) {
				analyticsLock.lock();
				rfTransferedIn += received;
				analyticsLock.unlock();
			}
			return received;
		}

		return 0;
	}

	/**
	 * Redirects the extractEnergy call to the neighboring TileEntity of the
	 * corresponding FrameConnector wall block if possible.
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IEnergyProvider) {
			int extracted = ((IEnergyProvider) targetTE).extractEnergy(from, maxExtract, simulate);
			if (!simulate && !worldObj.isRemote) {
				analyticsLock.lock();
				rfTransferedOut += extracted;
				analyticsLock.unlock();
			}
			return extracted;
		}

		return 0;
	}

	/**
	 * Redirects the getEnergyStored call to the neighboring TileEntity of the
	 * corresponding FrameConnector wall block if possible.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

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
	 * corresponding FrameConnector wall block if possible.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IEnergyProvider) {
			int maxStored = ((IEnergyProvider) targetTE).getMaxEnergyStored(from);
			return maxStored;
		} else if (targetTE instanceof IEnergyReceiver) {
			int maxStored = ((IEnergyReceiver) targetTE).getMaxEnergyStored(from);
			return maxStored;
		}

		return 0;
	}

	///////////////////////
	// INVENTORY METHODS //
	///////////////////////

	private static final int SIDE_BITS = 3;

	private IInventory getInventoryOnInsideWall(int side) {
		if (worldObj.isRemote)
			return null;

		Pocket p = getPocket();
		if (p == null)
			return null;

		ForgeDirection fdSide = ForgeDirection.getOrientation(side);

		switch (p.getFlowState(fdSide)) {
			case ENERGY:
				TileEntity te = getFrameConnectorNeighborTileEntity(fdSide);
				if (te instanceof IInventory)
					return Utils.getInventory((IInventory) te);
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
		int maskedSide = slot & 0b111;
		int innerSlot = slot >> SIDE_BITS;

			IInventory inventory = getInventoryOnInsideWall(maskedSide);
			if (inventory == null)
				return null;

			return inventory.getStackInSlot(innerSlot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		int maskedSide = slot & 0b111;
		int innerSlot = slot >> SIDE_BITS;

		IInventory inventory = getInventoryOnInsideWall(maskedSide);
		if (inventory == null)
			return null;

		return inventory.decrStackSize(innerSlot, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null; // not used, since the DP does not have an inventory GUI
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		int maskedSide = slot & 0b111;
		int innerSlot = slot >> SIDE_BITS;

		IInventory inventory = getInventoryOnInsideWall(maskedSide);
		if (inventory != null) {
			inventory.setInventorySlotContents(innerSlot, stack);
		}
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
		return 64;
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
		int side = slot & 0b111;
		slot = slot >> SIDE_BITS;

		IInventory inventory = getInventoryOnInsideWall(side);
		if (inventory == null)
			return false;

		return inventory.isItemValidForSlot(slot, stack);
	}

	// Sided inventory //

	private static final int[] EMPTY_SLOT_ARRAY = new int[0];
	// cache for inventory slot arrays to prevent frequent allocation of arrays.
	// REMOVE the caching IF it turns out something is modifying the cached arrays.
	// Using weak references so the GC can still recycle TEs that are only referenced here. (after their chunk unloaded f.e.)
	private transient List<WeakReference<IInventory>> lastKnownInventories;
	private transient int[][] lastKnownInventorySlots;

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side > 5)
			return EMPTY_SLOT_ARRAY;

		IInventory inventory = getInventoryOnInsideWall(side);
		if (inventory == null)
			return EMPTY_SLOT_ARRAY;

		// lazy init of "lastKnown" cache
		if (lastKnownInventories == null || lastKnownInventorySlots == null) {
			lastKnownInventories = new ArrayList<>(6);
			for (int i = 0; i < 6; i++) {
				lastKnownInventories.add(null);
			}
			lastKnownInventorySlots = new int[6][];
		}

		if (inventory instanceof ISidedInventory)
			return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side);

		// use cache for plain IInventory if possible
		WeakReference<IInventory> cachedInventory = lastKnownInventories.get(side);

		if (cachedInventory != null && cachedInventory.get() == inventory
				&& inventory.getSizeInventory() == lastKnownInventorySlots[side].length)
			return lastKnownInventorySlots[side];

		int[] slots = new int[inventory.getSizeInventory()];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = (i << SIDE_BITS) | side;
		}

		// store in cache
		lastKnownInventories.set(side, new WeakReference<>(inventory));
		lastKnownInventorySlots[side] = slots;

		return slots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		int maskedSide = slot & 0b111;
		if (maskedSide != side)
			return false;

		int innerSlot = slot >> SIDE_BITS;

			IInventory inventory = getInventoryOnInsideWall(side);
			if (inventory == null)
				return false;

			return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canInsertItem(innerSlot, stack, side);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		int maskedSide = slot & 0b111;
		if (maskedSide != side)
			return false;

		int innerSlot = slot >> SIDE_BITS;

		IInventory inventory = getInventoryOnInsideWall(side);
		if (inventory == null)
			return false;

		return !(inventory instanceof ISidedInventory) ? true : ((ISidedInventory) inventory).canExtractItem(innerSlot, stack, side);
	}

	//////////////////
	// WORMHOLE API //
	//////////////////

	@Override
	public List<WormholeTarget<Block, TileEntity>> getAllTargets(ForgeDirection fromDirection) {
		if (worldObj.isRemote)
			return Collections.emptyList();

		Pocket p = getPocket();
		if (p == null)
			return Collections.emptyList();

		World targetWorld = MinecraftServer.getServer().worldServerForDimension(Reference.DIMENSION_ID);

		CoordSet targetCoords = p.getConnectorCoords(fromDirection);

		if (targetCoords == null)
			return Collections.emptyList();

		ForgeDirection toDirection = fromDirection.getOpposite();
		targetCoords.addForgeDirection(toDirection);

		List<WormholeTarget<Block, TileEntity>> wormholeTargets = new ArrayList<>(1);
		wormholeTargets.add(new WormholeTarget<>(targetWorld, targetCoords.x, targetCoords.y, targetCoords.z, toDirection));

		return wormholeTargets;
	}

	///////////////////
	// OpenComputers //
	///////////////////

	@Optional.Method(modid = "OpenComputers")
	@Override
	public Node sidedNode(ForgeDirection side) {
		if (worldObj.isRemote)
			return null;

		Pocket p = getPocket();
		if (p == null)
			return null;

		switch (p.getFlowState(side)) {
			case ENERGY:
				TileEntity te = getFrameConnectorNeighborTileEntity(side);
				if (te instanceof SidedEnvironment)
					return ((SidedEnvironment) te).sidedNode(side);
				else if (te instanceof Environment)
					return ((Environment) te).node();
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
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler) {
			int filled = ((IFluidHandler) targetTE).fill(from, resource, doFill);
			if (doFill && !worldObj.isRemote) {
				analyticsLock.lock();
				fluidsTransferedIn += filled;
				analyticsLock.unlock();
			}
			return filled;
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler) {
			FluidStack drained = ((IFluidHandler) targetTE).drain(from, resource, doDrain);
			if (doDrain && !worldObj.isRemote) {
				analyticsLock.lock();
				fluidsTransferedOut += drained.amount;
				analyticsLock.unlock();
			}
			return drained;
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler) {
			FluidStack drained = ((IFluidHandler) targetTE).drain(from, maxDrain, doDrain);
			if (doDrain && !worldObj.isRemote) {
				analyticsLock.lock();
				fluidsTransferedOut += drained.amount;
				analyticsLock.unlock();
			}
			return drained;
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler)
			return ((IFluidHandler) targetTE).canFill(from, fluid);
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler)
			return ((IFluidHandler) targetTE).canDrain(from, fluid);
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		TileEntity targetTE = getFrameConnectorNeighborTileEntity(from);

		if (targetTE instanceof IFluidHandler)
			return ((IFluidHandler) targetTE).getTankInfo(from);
		return null;
	}
}
