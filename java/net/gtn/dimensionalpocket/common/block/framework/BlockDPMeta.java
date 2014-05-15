package net.gtn.dimensionalpocket.common.block.framework;

import java.util.List;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockDPMeta extends BlockDP {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    protected BlockDPMeta(Material material, String name) {
        super(material, name);
    }

    @Override
    public void register(String name) {
        GameRegistry.registerBlock(this, getItemBlockClass(), name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        String[] names = getNames();
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String[] names = getNames();
        icons = new IIcon[names.length];
        for (int i = 0; i < names.length; i++)
            icons[i] = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + names[MathHelper.clamp_int(i, 0, names.length)]);
    }

    @Override
    public int damageDropped(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[meta % icons.length];
    }

    public abstract String[] getNames();

    protected abstract Class<? extends ItemBlock> getItemBlockClass();

}
