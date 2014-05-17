package net.gtn.dimensionalpocket.client;

import net.gtn.dimensionalpocket.client.particles.PlayerStreamFX;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocket;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UtilFX {

    public static void createPlayerStream(EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        EntityFX fx = new PlayerStreamFX(player.worldObj, player, targetSet, ticksToTake);

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }

    public static IIcon getDimensionalParticle() {
        return ((BlockDimensionalPocket) ModBlocks.dimensionalPocket).particle;
    }

}
