package net.gtn.dimensionalpocket.common.items;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.interfaces.IUsable;
import net.gtn.dimensionalpocket.common.items.framework.ItemDP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUsable extends ItemDP {

    private final IUsable handler;

    public ItemUsable(String name, IUsable handler) {
        super(name);
        this.handler = handler;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return handler.onItemUse(itemStack, player, world, new CoordSet(x, y, z), side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return handler.onItemUseFirst(itemStack, player, world, new CoordSet(x, y, z), side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return handler.onItemRightClick(itemStack, world, player);
    }
}
