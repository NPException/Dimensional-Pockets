package net.gtn.dimensionalpocket.common.items.framework;

import me.jezza.oc.common.items.ItemInformation;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.interfaces.IUsable;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public abstract class UsableHandlerAbstract implements IUsable {

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        // do nothing
    }
    
    protected void addShiftInformation(ItemInformation information) {
        information.addInfoList(Utils.translate("info.tooltip.shift", EnumChatFormatting.DARK_GRAY, EnumChatFormatting.GRAY));
    }
}
