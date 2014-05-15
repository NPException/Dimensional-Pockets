package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.common.items.framework.ItemDP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemInfoTool extends ItemDP {

    public ItemInfoTool(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        onRightClick(player);
        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        onRightClick(player);
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public void onRightClick(EntityPlayer player) {
        
    }

}
