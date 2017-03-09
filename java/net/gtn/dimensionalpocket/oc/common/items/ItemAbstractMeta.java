package net.gtn.dimensionalpocket.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.common.utils.MathHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

@Deprecated
public abstract class ItemAbstractMeta extends ItemAbstract {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    protected ItemAbstractMeta(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        List<String> names = getNames();
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        List<String> names = getNames();
        icons = new IIcon[names.size()];
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(modIdentifier + names.get(i));
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        List<String> names = getNames();
        damage = MathHelper.clipInt(damage, names.size() - 1);
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        List<String> names = getNames();
        int damage = MathHelper.clipInt(itemStack.getItemDamage(), names.size() - 1);
        return "item." + names.get(damage);
    }

    public boolean isValidStack(ItemStack itemStack, String name) {
        int damage = itemStack.getItemDamage();
        try {
            return name.equals(getNames().get(damage));
        } catch (Exception e) {
        }
        return false;
    }

    public abstract List<String> getNames();

}
