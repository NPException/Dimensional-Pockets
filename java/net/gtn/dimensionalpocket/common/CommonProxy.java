package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

    private static final String GIVEN_INFO_BOOK = "givenInfoBook";

    public void runServerSide() {
        registerTileEntities();
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileDimensionalPocket.class, Strings.BLOCK_POCKET);
    }

    public void runClientSide() {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        World world = event.world;

        if (!world.isRemote && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            NBTTagCompound persistTag = Utils.getPlayerPersistTag(player);

            boolean shouldGiveManual = Reference.SHOULD_SPAWN_WITH_BOOK && !persistTag.getBoolean(GIVEN_INFO_BOOK);
            if (shouldGiveManual) {
                ItemStack infoBook = new ItemStack(ModItems.book);
                if (!player.inventory.addItemStackToInventory(infoBook)) {
                    World playerWorld = player.worldObj;
                    EntityItem entityItem = new EntityItem(playerWorld, player.posX, player.posY, player.posZ, infoBook);
                    entityItem.delayBeforeCanPickup = 0;

                    playerWorld.spawnEntityInWorld(entityItem);
                }
                persistTag.setBoolean(GIVEN_INFO_BOOK, true);
            }
        }
    }

}
