package net.gtn.dimensionalpocket.common.lib;

import net.minecraft.util.ResourceLocation;

public class GuiSheet {

    private static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_INFO_BOOK = getResource("guideDP");
    public static final ResourceLocation GUI_INFO_BOOK_CRAFTING = getResource("craftingGrid");

    private static ResourceLocation getResource(String loc) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), GUI_SHEET_LOCATION + loc + ".png");
    }

}
