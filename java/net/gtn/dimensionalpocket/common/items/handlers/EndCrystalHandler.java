package net.gtn.dimensionalpocket.common.items.handlers;

import java.util.List;

import me.jezza.oc.common.items.ItemInformation;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class EndCrystalHandler extends UsableHandlerAbstract {

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.dimension != Reference.DIMENSION_ID)
            return itemStack;

        if (world.isRemote) {
            player.swingItem();
            return itemStack;
        }

        CoordSet coordSet = new CoordSet(player);
        Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
        if (pocket == null)
            return itemStack;

        pocket.setSpawnInPocket(coordSet.toChunkOffset(), player.rotationYaw, player.rotationPitch);
        
        ChatComponentTranslation comp = new ChatComponentTranslation("info.spawn.set.in.pocket");
        comp.getChatStyle().setItalic(Boolean.TRUE);
        player.addChatMessage(comp);
            
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        addShiftInformation(information);
        
        String text = Utils.translate("info.tooltip.endCrystal.shift");
        List<String> lines = Utils.formatToLines(text, 40);
        for (String line : lines) {
            information.addShiftList(line);
        }
    }
}
