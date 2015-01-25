package net.gtn.dimensionalpocket.common.core.utils;

import java.util.ArrayList;
import java.util.List;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {

    public static ForgeDirection getDirectionFromBitMask(int num) {
        switch (num) {
            case 0:
                return ForgeDirection.SOUTH;
            case 1:
                return ForgeDirection.WEST;
            case 2:
                return ForgeDirection.NORTH;
            case 3:
                return ForgeDirection.EAST;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }

    public static String capitalizeString(String string) {
        if (string == null || string.isEmpty())
            return "";

        String firstLetter = string.substring(0, 1).toUpperCase();
        if (string.length() == 1)
            return firstLetter;

        String rest = string.substring(1).toLowerCase();
        return firstLetter + rest;
    }
    
    public static String translate(String key) {
        return translate(key, (Object[])null);
    }
    
    public static String translate(String key, Object ... params) {
        String result = StatCollector.translateToLocal(key);
        if (params != null) {
            for(int i=0; i<params.length; i++) {
                result = result.replace("{"+(i)+"}", String.valueOf(params[i]));
            }
        }
        return result;
    }

    public static NBTTagCompound getPlayerPersistTag(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData();

        NBTTagCompound persistTag;
        if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        } else {
            persistTag = new NBTTagCompound();
            tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
        }

        NBTTagCompound modTag;
        String modID = Reference.MOD_ID;

        if (persistTag.hasKey(modID)) {
            modTag = persistTag.getCompoundTag(modID);
        } else {
            modTag = new NBTTagCompound();
            persistTag.setTag(modID, modTag);
        }

        return modTag;
    }

    /**
     * This method will write the given name and lore to the itemstack's "display"-nbt tag. (Thanks to oku)
     *
     * @param stack
     * @param name
     * @param loreStrings
     * @return
     */
    public static ItemStack generateItem(ItemStack stack, String name, boolean forceCleanName, String... loreStrings) {
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagCompound display;
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        if (!stack.getTagCompound().hasKey("display")) {
            stack.setTagInfo("display", new NBTTagCompound());
        }

        display = stack.getTagCompound().getCompoundTag("display");

        if (loreStrings != null && loreStrings.length > 0) {
            NBTTagList lore = new NBTTagList();
            for (String s : loreStrings)
                lore.appendTag(new NBTTagString(EnumChatFormatting.GRAY + s));
            display.setTag("Lore", lore);
        }

        if (name != null) {
            StringBuilder sb = new StringBuilder();
            if (forceCleanName)
                sb.append(EnumChatFormatting.RESET);
            sb.append(name);

            display.setString("Name", sb.toString());
        }

        return stack;
    }

    /**
     * Spawns an itemStack in the world.
     */
    public static void spawnItemStack(ItemStack itemStack, World world, float x, float y, float z, int delayBeforePickup) {
        EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);
        entityItem.delayBeforeCanPickup = delayBeforePickup;

        world.spawnEntityInWorld(entityItem);
    }
    
    /**
     * Tries to check if this is a server side call. If it is a remote call, this throws an exception.
     */
    public static void enforceServer() {
        if (Reference.ENFORCE_SIDED_METHODS) {
            Minecraft mc = Minecraft.getMinecraft();
            if (!mc.isIntegratedServerRunning() && (mc.theWorld != null && mc.theWorld.isRemote)) {
                throw new RuntimeException("DONT YOU DARE CALL THIS METHOD ON A CLIENT!");
            }
        }
    }
    
    /**
     * Tries to check if this is a client side call. If it is a non remote call, this throws an exception.
     */
    public static void enforceClient() {
        if (Reference.ENFORCE_SIDED_METHODS) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld != null && !mc.theWorld.isRemote) {
                throw new RuntimeException("DONT YOU DARE CALL THIS METHOD ON A CLIENT!");
            }
        }
    }
    
    public static boolean isOreDictItem(ItemStack stack, String oreDictName) {
        enforceServer();
        int targetOreDictID = OreDictionary.getOreID(oreDictName);
        for (int stackOreDictID : OreDictionary.getOreIDs(stack)) {
            if (targetOreDictID == stackOreDictID)
                return true;
        }
        return false;
    }
    
    public static boolean isItemPocketWrench(ItemStack stack) {
        if (!Utils.isOreDictItem(stack, "stickWood"))
            return false;
        
        if (!stack.hasTagCompound())
            return false;
        
        NBTTagCompound itemCompound = stack.getTagCompound();
        if (!itemCompound.hasKey("display")) 
            return false;
        
        String customName = itemCompound.getCompoundTag("display").getString("Name");
        return "Pocket Wrench".equalsIgnoreCase(customName);
    }

    public static List<String> formatToLines(String text, int maxLineLength) {
       List<String> lines = new ArrayList<>();
       for (String part : text.split("\\\\n")) {
           if (part.length() <= maxLineLength) {
               lines.add(part);
           } else {
               String[] words = part.split(" ");
               StringBuilder sb = new StringBuilder();
               for (String word : words) {
                   String space = (sb.length() == 0) ? "" : " ";
                   if (sb.length() + space.length() + word.length() > maxLineLength) {
                       lines.add(sb.toString());
                       sb = new StringBuilder();
                       space = "";
                   }
                   sb.append(space).append(word);
               }
               lines.add(sb.toString());
           }
       }
       return lines;
    }
}
