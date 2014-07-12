package net.gtn.dimensionalpocket.client.utils;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSheet {

    private static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_INFO_BOOK = getResource("guideDP");
    public static final ResourceLocation GUI_CONFIG = getResource("configGui");

    private static ResourceLocation getResource(String loc) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), GUI_SHEET_LOCATION + loc + ".png");
    }

}
