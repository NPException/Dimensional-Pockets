package net.gtn.dimensionalpocket.common.items.handlers;

import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.items.framework.UsableHandlerAbstract;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class EnderCrystalHandler extends UsableHandlerAbstract {

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, CoordSet coordSet, int side, float hitX, float hitY, float hitZ) {
        if (player.dimension != Reference.DIMENSION_ID)
            return false;

        if (world.isRemote) {
            player.swingItem();
            return false;
        }

        Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
        if (pocket == null)
            return false;

        CoordSet spawnCoords = coordSet.asChunkOffset();

        int sx = spawnCoords.getX();
        int sy = spawnCoords.getY();
        int sz = spawnCoords.getZ();

        // compensate for wall and ceiling

        //@formatter:off
        if      (sx == 0)  coordSet.addX(1).addY(-1);
        else if (sx == 15) coordSet.addX(-1).addY(-1);
        else if (sz == 0)  coordSet.addZ(1).addY(-1);
        else if (sz == 15) coordSet.addZ(-1).addY(-1);
        else if (sy == 15) coordSet.addY(-3);

        spawnCoords = coordSet.asChunkOffset();

        boolean flag = world.isAirBlock(coordSet.getX(), coordSet.getY() + 1, coordSet.getZ())
                       && world.isAirBlock(coordSet.getX(), coordSet.getY() + 2, coordSet.getZ())
                       && spawnCoords.getY() <= 12;
        //@formatter:on

        if (flag) {
            pocket.setSpawnCoords(spawnCoords);
//            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            ChatComponentTranslation comp = new ChatComponentTranslation("info.spawn.set.in.pocket");
            comp.getChatStyle().setItalic(Boolean.TRUE);
            player.addChatMessage(comp);
        }
        return flag;
    }

}
