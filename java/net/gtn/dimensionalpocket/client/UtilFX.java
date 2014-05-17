package net.gtn.dimensionalpocket.client;

import net.gtn.dimensionalpocket.client.particles.PlayerStreamFX;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UtilFX {

    public static void createParticleStream(EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        EntityFX playerStream = new PlayerStreamFX(player.worldObj, player, targetSet, ticksToTake);
        Minecraft.getMinecraft().effectRenderer.addEffect(playerStream);
    }

}
