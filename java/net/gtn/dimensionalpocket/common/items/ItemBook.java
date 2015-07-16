package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBook extends ItemDP {

    public ItemBook(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            player.openGui(DimensionalPockets.instance, 0, world, 0, 0, 0);
        }
        return itemStack;
    }
}
