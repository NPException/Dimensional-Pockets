package net.gtn.dimensionalpocket.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.common.block.BlockDPMeta;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

/**
 * You shouldn't need this class at all.
 * If you think you do, you don't.
 * @author Jeremy
 *
 */
public abstract class ItemBlockDPMeta extends ItemBlock {

    private Block block;

    public ItemBlockDPMeta(Block block) {
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
    public String getUnlocalizedName(ItemStack itemStack) {
        if (!(block instanceof BlockDPMeta))
            super.getUnlocalizedName(itemStack);
        String[] names = ((BlockDPMeta) block).getNames();
        return "item." + names[MathHelper.clamp_int(itemStack.getItemDamage(), 0, names.length - 1)];
    }

}
