package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.items.framework.ItemDPMeta;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMisc extends ItemDPMeta {

    private static final String[] names = new String[] { "enderCrystal", "netherCrystal", "infoTool" };

    public ItemMisc(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote && itemStack.getItemDamage() == 2)
            player.openGui(DimensionalPockets.instance, 0, world, 0, 0, 0);
        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || player.dimension != Reference.DIMENSION_ID)
            return false;

        if (itemStack.getItem() == ModItems.miscItems && (itemStack.getItemDamage() == 0 || itemStack.getItemDamage() == 1)) {
            CoordSet blockSet = new CoordSet(x, y, z);

            Pocket pocket = PocketRegistry.getPocket(blockSet.toChunkCoords());
            if (pocket == null)
                return false;

            CoordSet spawnSet = blockSet.asSpawnPoint();

            int sx = spawnSet.getX();
            int sy = spawnSet.getY();
            int sz = spawnSet.getZ();

            //@formatter:off
            // compensate for wall and ceiling

            if      (sx == 0)  blockSet.addX(1).addY(-1);
            else if (sx == 15) blockSet.addX(-1).addY(-1);
            else if (sz == 0)  blockSet.addZ(1).addY(-1);
            else if (sz == 15) blockSet.addZ(-1).addY(-1);
            else if (sy == 15) blockSet.addY(-3);
            
            spawnSet = blockSet.asSpawnPoint();

            boolean flag = world.isAirBlock(blockSet.getX(), blockSet.getY() + 1, blockSet.getZ())
                            && world.isAirBlock(blockSet.getX(), blockSet.getY() + 2, blockSet.getZ())
                            && spawnSet.getY() <= 12;
            //@formatter:on

            if (flag) {
                pocket.setSpawnSet(spawnSet);

                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                return true;
            }
        }

        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitVecX, float hitVecY, float hitVecZ) {
        return false;
    }

    @Override
    public String[] getNames() {
        return names;
    }

}
