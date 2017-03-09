package net.gtn.dimensionalpocket.oc.common.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.oc.common.interfaces.IItemTooltip;
import net.gtn.dimensionalpocket.oc.common.utils.MovingObjectPositionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;

public abstract class ItemAbstract extends Item {
    protected boolean textureReg = true;
    protected boolean hasEffect = false;
    public final String modIdentifier;

    public ItemAbstract(String name) {
        modIdentifier = Loader.instance().activeModContainer().getModId() + ":";
        setName(name);
        register(name);
    }

    public ItemAbstract setName(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
        return this;
    }

    public ItemAbstract register(String name) {
        GameRegistry.registerItem(this, name);
        return this;
    }

    public ItemAbstract setTextureless() {
        this.textureReg = false;
        return this;
    }

    public ItemAbstract setEffect() {
        this.hasEffect = true;
        return this;
    }

    public ItemAbstract setShapelessRecipe(Object... items) {
        return setShapelessRecipe(1, items);
    }

    public ItemAbstract setShapelessRecipe(int resultSize, Object... items) {
        return setShapelessRecipe(resultSize, 0, items);
    }

    public ItemAbstract setShapelessRecipe(int resultSize, int meta, Object... items) {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this, resultSize, meta), items);
        return this;
    }

    public MovingObjectPosition getMOP(EntityLivingBase entity) {
        return MovingObjectPositionHelper.getCurrentMovingObjectPosition(entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return itemStack.isItemEnchanted() || hasEffect;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        if (textureReg)
            itemIcon = iconRegister.registerIcon(modIdentifier + getIconString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        information.populateList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, IItemTooltip tooltip) {
    }
}