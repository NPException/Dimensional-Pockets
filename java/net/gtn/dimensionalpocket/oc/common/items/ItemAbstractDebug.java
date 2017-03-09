package net.gtn.dimensionalpocket.oc.common.items;

import net.gtn.dimensionalpocket.oc.common.interfaces.IItemTooltip;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class ItemAbstractDebug extends ItemAbstract {

    private int debugMode = 0;

    public ItemAbstractDebug(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ItemAbstract register(String name) {
//        if (DeusCore.isDebugMode())
        return super.register(name);
//        return this;
    }

    public int getDebugMode() {
        return debugMode;
    }

    private String getDebugString() {
        return getCurrentDebugString() + " Mode";
    }

    public String getCurrentDebugString() {
        return getDebugList().get(debugMode);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
//        if (DeusCore.isDebugMode()) {
        if (player.isSneaking()) {
            if (++debugMode == getDebugList().size())
                debugMode = 0;
            player.addChatComponentMessage(new ChatComponentText(getDebugString()));
        }
//        }
        return itemStack;
    }

    @Override
    public abstract boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ);

    @Override
    public abstract boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ);

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip information) {
        information.addToInfoList("Debug Mode: " + debugMode);
        information.addToInfoList(getDebugString());
    }

    public void addChatMessage(EntityPlayer player, String string) {
        player.addChatComponentMessage(new ChatComponentText(string));
    }

    public abstract ArrayList<String> getDebugList();
}