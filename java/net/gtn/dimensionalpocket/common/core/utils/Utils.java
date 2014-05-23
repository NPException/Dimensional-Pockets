package net.gtn.dimensionalpocket.common.core.utils;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Utils {

    public static NBTTagCompound getPlayerPersistTag(EntityPlayer player) {

        NBTTagCompound tag = player.getEntityData();

        NBTTagCompound persistTag = null;
        if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        } else {
            persistTag = new NBTTagCompound();
            tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
        }

        NBTTagCompound modTag = null;
        String modID = Reference.MOD_ID;

        if (persistTag.hasKey(modID)) {
            modTag = persistTag.getCompoundTag(modID);
        } else {
            modTag = new NBTTagCompound();
            persistTag.setTag(modID, modTag);
        }

        return modTag;
    }

}
