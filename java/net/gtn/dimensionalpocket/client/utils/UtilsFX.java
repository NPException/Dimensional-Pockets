package net.gtn.dimensionalpocket.client.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.gtn.dimensionalpocket.client.particles.PlayerStreamFX;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UtilsFX {

    static Map<String, ResourceLocation> boundTextures = new HashMap();

    public static void createPlayerStream(EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        EntityFX fx = new PlayerStreamFX(player.worldObj, player, targetSet, ticksToTake, new Random(), new Colour(0.4F, 0.4F, 0.4F, 0.5F));

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }

    public static void createPlayerExplosion(EntityPlayer player) {

    }

    public static void bindTexture(String texture) {
        ResourceLocation resource = null;
        String key = "textures/misc/" + texture + ".png";

        if (boundTextures.containsKey(key)) {
            resource = boundTextures.get(key);
        } else {
            resource = new ResourceLocation(Reference.MOD_IDENTIFIER + key);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }

    public static ResourceLocation getParticleTexture() {
        try {
            return (ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null, new String[] { "particleTextures", "b", "field_110737_b" });
        } catch (Exception e) {
        }
        return null;
    }
}
