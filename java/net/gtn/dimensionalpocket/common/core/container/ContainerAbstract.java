package net.gtn.dimensionalpocket.common.core.container;

import net.gtn.dimensionalpocket.common.tileentity.TileDP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;


public abstract class ContainerAbstract extends Container {

	private TileDP tile;

	public ContainerAbstract(TileEntity tile) {
		this.tile = (TileDP) tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
}
