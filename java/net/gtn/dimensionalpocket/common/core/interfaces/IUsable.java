package net.gtn.dimensionalpocket.common.core.interfaces;

import me.jezza.oc.common.items.ItemInformation;
import me.jezza.oc.common.utils.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * All methods are called on both client and server.
 * 
 * Don't implement this.
 * Instead extend UsableHandlerAbstract
 */
public interface IUsable {

    //@formatter:off
    /**
     * Callback for item usage.
     * Return true if you did anything and false if you didn't. 
     * This is for ITEMS, not BLOCKS
     * Default return: false;
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ);

    /**
     * Called before the block.
     * If returns true, everything else will stop processing.
     * Default return: false;
     */
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ);

    /**
     * Called whenever the item is rightClicked.
     * Could be on a block or just in the world.
     * Default return: itemStack;
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player);
    //@formatter:on

    
    /**
     * Called by OmnisCore to get ToolTip information for an Item
     * @param stack
     * @param player
     * @param information
     */
    public void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information);
}
