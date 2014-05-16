package net.gtn.dimensionalpocket.common.items.framework;

import java.util.List;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemDPMeta extends ItemDP {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemDPMeta(String name) {
        super(name);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        String[] names = getNames();
        int i = 0;
        for (String name : names)
            list.add(new ItemStack(this, 1, i++));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        String[] names = getNames();
        icons = new IIcon[names.length];
        int i = 0;
        for (String name : names)
            icons[i++] = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String[] names = getNames();
        return "item." + names[itemStack.getItemDamage()];
    }

    public abstract String[] getNames();

}
