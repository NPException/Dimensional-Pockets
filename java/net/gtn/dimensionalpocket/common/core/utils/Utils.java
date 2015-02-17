package net.gtn.dimensionalpocket.common.core.utils;

import me.jezza.oc.client.gui.lib.Colour;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumMap;

public class Utils {

    public static EnumMap<ForgeDirection, Colour> FD_COLOURS = new EnumMap<>(ForgeDirection.class);

    static {
        // @formatter:off
        double alpha = 7.0;
        Colour purple = Colour.PURPLE.copy(); purple.a = alpha;
        Colour orange = Colour.ORANGE.copy(); orange.a = alpha;
        Colour yellow = Colour.YELLOW.copy(); yellow.a = alpha;
        Colour blue = Colour.BLUE.copy(); blue.a = alpha;
        Colour green = Colour.GREEN.copy(); green.a = alpha;
        Colour red = Colour.RED.copy(); red.a = alpha;
        
        FD_COLOURS.put(ForgeDirection.DOWN,  purple);
        FD_COLOURS.put(ForgeDirection.UP,    orange);
        FD_COLOURS.put(ForgeDirection.NORTH, yellow);
        FD_COLOURS.put(ForgeDirection.SOUTH, blue);
        FD_COLOURS.put(ForgeDirection.WEST,  green);
        FD_COLOURS.put(ForgeDirection.EAST,  red);
        // @formatter:on
    }

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
     * This method will write the given name and lore to the itemStack's "display"-nbt tag. (Thanks to oku)
     */
    public static ItemStack generateItem(ItemStack itemStack, String name, boolean forceCleanName, String... loreStrings) {
        NBTTagCompound nbt = itemStack.getTagCompound();
        NBTTagCompound display;
        if (nbt == null) {
            nbt = new NBTTagCompound();
            itemStack.setTagCompound(nbt);
        }
        if (!itemStack.getTagCompound().hasKey("display")) {
            itemStack.setTagInfo("display", new NBTTagCompound());
        }

        display = itemStack.getTagCompound().getCompoundTag("display");

        if (loreStrings != null && loreStrings.length > 0) {
            NBTTagList lore = new NBTTagList();
            for (String s : loreStrings) {
                if (s != null)
                    lore.appendTag(new NBTTagString(EnumChatFormatting.GRAY + s));
            }
            display.setTag("Lore", lore);
        }

        if (name != null) {
            StringBuilder sb = new StringBuilder();
            if (forceCleanName)
                sb.append(EnumChatFormatting.RESET);
            sb.append(name);

            display.setString("Name", sb.toString());
        }

        return itemStack;
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

    public static ChatComponentText createChatLink(String text, String url, boolean bold, boolean underline, boolean italic, EnumChatFormatting color) {
        ChatComponentText link = new ChatComponentText(text);
        ChatStyle style = link.getChatStyle();
        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        style.setBold(bold);
        style.setUnderlined(underline);
        style.setItalic(italic);
        style.setColor(color);
        return link;
    }

    /**
     * Ensures that the given inventory is the full inventory, i.e. takes double
     * chests into account.<br>
     * <i>METHOD COPIED FROM BUILDCRAFT</i>
     *
     * @param inv
     * @return Modified inventory if double chest, unmodified otherwise.
     */
    public static IInventory getInventory(IInventory inv) {
        if (inv instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) inv;

            TileEntityChest adjacent = null;

            if (chest.adjacentChestXNeg != null) {
                adjacent = chest.adjacentChestXNeg;
            }

            if (chest.adjacentChestXPos != null) {
                adjacent = chest.adjacentChestXPos;
            }

            if (chest.adjacentChestZNeg != null) {
                adjacent = chest.adjacentChestZNeg;
            }

            if (chest.adjacentChestZPos != null) {
                adjacent = chest.adjacentChestZPos;
            }

            if (adjacent != null) {
                return new InventoryLargeChest("", inv, adjacent);
            }
            return inv;
        }
        return inv;
    }
}
