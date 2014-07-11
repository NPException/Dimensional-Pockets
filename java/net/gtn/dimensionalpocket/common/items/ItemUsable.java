package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.common.core.interfaces.IUsable;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.items.framework.ItemDP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUsable extends ItemDP {

    private IUsable handler;

    public ItemUsable(String name) {
        super(name);
    }

    public ItemUsable setHandler(IUsable handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (handler != null)
            return handler.onItemUse(itemStack, player, world, new CoordSet(x, y, z), side, hitX, hitY, hitZ);
        return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (handler != null)
            return handler.onItemUseFirst(itemStack, player, world, new CoordSet(x, y, z), side, hitX, hitY, hitZ);
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (handler != null)
            return handler.onItemRightClick(itemStack, world, player);
        return super.onItemRightClick(itemStack, world, player);
    }
}
