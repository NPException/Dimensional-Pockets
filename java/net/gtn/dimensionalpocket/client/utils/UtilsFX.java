package net.gtn.dimensionalpocket.client.utils;

import java.util.HashMap;
import java.util.Map;

import net.gtn.dimensionalpocket.client.particles.PlayerStreamFX;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UtilsFX {

    static Map<String, ResourceLocation> boundTextures = new HashMap();

    public static void createPlayerStream(EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        EntityFX fx = new PlayerStreamFX(player.worldObj, player, targetSet, ticksToTake);

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }

    public static void bindTexture(String texture) {
        ResourceLocation resource = null;
        String key = "textures/misc/" + texture + ".png";

        if (boundTextures.containsKey(key)) {
            resource = boundTextures.get(key);
        } else {
            resource = new ResourceLocation(Reference.MOD_ID, key);
            boundTextures.put(key, resource);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }
}
