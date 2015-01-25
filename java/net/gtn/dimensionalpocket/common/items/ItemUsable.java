package net.gtn.dimensionalpocket.common.items;

import me.jezza.oc.common.items.ItemAbstract;
import me.jezza.oc.common.items.ItemInformation;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.core.interfaces.IUsable;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUsable extends ItemAbstract {

    private final IUsable handler;
    public boolean hasEffect = false;

    public ItemUsable(String name, IUsable handler) {
        super(name);
        setCreativeTab(DimensionalPockets.creativeTab);
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

    @Override
    public String getModIdentifier() {
        return Reference.MOD_IDENTIFIER;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return hasEffect;
    }
    
    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        handler.addInformation(stack, player, information);
    }
}
