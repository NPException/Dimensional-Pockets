package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemInfoTool;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.item.Item;

public class ModItems {

    public static Item infoTool;

    public static void init() {
        infoTool = new ItemInfoTool(Strings.ITEM_INFO_TOOL);
    }

}
