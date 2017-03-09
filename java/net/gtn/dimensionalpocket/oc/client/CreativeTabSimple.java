package net.gtn.dimensionalpocket.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabSimple extends CreativeTabs {

    private Item icon = Items.apple;
    private int damage = 0;

    public CreativeTabSimple(String label) {
        super(label);
    }

    public CreativeTabSimple setIcon(Item icon) {
        this.icon = icon;
        return this;
    }

    public CreativeTabSimple setIcon(Item icon, int damage) {
        this.icon = icon;
        this.damage = damage;
        return this;
    }

    public CreativeTabSimple setIcon(ItemStack itemStack) {
        this.icon = itemStack.getItem();
        this.damage = itemStack.getItemDamage();
        return this;
    }

    public CreativeTabSimple setIcon(Block block) {
        return setIcon(Item.getItemFromBlock(block));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int func_151243_f() {
        return damage;
    }
}
