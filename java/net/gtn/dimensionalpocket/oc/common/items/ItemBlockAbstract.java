package net.gtn.dimensionalpocket.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.common.interfaces.IItemTooltip;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemBlockAbstract extends ItemBlock {

    public ItemBlockAbstract(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public Block getBlock() {
        return field_150939_a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return getBlock().getIcon(2, damage);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        information.populateList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip information) {
    }
}
