package net.gtn.dimensionalpocket.oc.client.lib;

import net.gtn.dimensionalpocket.oc.common.core.CoreProperties;
import net.minecraft.util.ResourceLocation;

public class ResourceHelper {

    public static ResourceLocation getMinecraftResource(String texture) {
        return new ResourceLocation(texture);
    }

    public static ResourceLocation getOCTexture(String texture) {
        return getOCResource("textures/" + texture);
    }

    public static ResourceLocation getOCResource(String texture) {
        return getModResource(CoreProperties.MOD_ID, texture);
    }

    public static ResourceLocation getModResource(String modID, String texture) {
        return new ResourceLocation(modID + ":" + texture);
    }

}
