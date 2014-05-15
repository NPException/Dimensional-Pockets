package net.gtn.dimensionalpocket.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockDP extends ItemBlock {

    private Block block;

    public ItemBlockDP(Block block) {
        super(block);
        this.block = block;
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return block.getIcon(2, damage);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        // TODO Auto-generated method stub
        return super.getUnlocalizedName(par1ItemStack);
    }

}
