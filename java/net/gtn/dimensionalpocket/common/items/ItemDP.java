package net.gtn.dimensionalpocket.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public abstract class ItemDP extends Item {

    public ItemDP(String name) {
        setUnlocalizedName(name);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerItem(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + getUnlocalizedName().replace("item.", ""));
    }

}
