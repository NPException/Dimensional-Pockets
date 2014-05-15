package net.gtn.dimensionalpocket.common.items.framework;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.common.core.CoordSet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Use this to create the itemBlock for metas.
 * 
 * @author Jeremy
 * 
 */
public class ItemBlockHolder extends ItemBlockDPMeta {

    public ItemBlockHolder(Block block) {
        super(block);
    }
}
