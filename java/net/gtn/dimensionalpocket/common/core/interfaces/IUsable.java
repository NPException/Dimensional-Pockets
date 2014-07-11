package net.gtn.dimensionalpocket.common.core.interfaces;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUsable {

    /**
     * Default return: false;
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ);

    /**
     * Default return: false;
     */
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ);

    /**
     * Default return: itemStack;
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player);
}
