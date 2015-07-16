package net.gtn.dimensionalpocket.client.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.jezza.oc.client.gui.lib.Colour;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.particles.PlayerStreamFX;
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

	private static Map<String, ResourceLocation> boundTextures = new HashMap<>();

	public static void bindTexture(String texture) {
		ResourceLocation resource = null;
		String key = "textures/misc/" + texture + ".png";

		if (boundTextures.containsKey(key)) {
			resource = boundTextures.get(key);
		} else {
			resource = new ResourceLocation(Reference.MOD_IDENTIFIER + key);
			boundTextures.put(texture, resource);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
	}

	public static void createPlayerStream(EntityPlayer player, CoordSet targetSet, int ticksToTake) {
		EntityFX fx = new PlayerStreamFX(player.worldObj, player, targetSet, ticksToTake, new Random(), Colour.WHITE);

		Minecraft.getMinecraft().effectRenderer.addEffect(fx);
	}

	public static ResourceLocation getParticleTexture() {
		try {
			return (ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null, "particleTextures", "b", "field_110737_b");
		} catch (Exception e) {
			DPLogger.warning("Could not load particleTextures");
			e.printStackTrace();
		}
		return null;
	}
}
