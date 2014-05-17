package net.gtn.dimensionalpocket.client.particles;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerStreamFX extends EntityFX {

    private static final Colour COLOUR = new Colour(0.5F, 0.5F, 0.5F, 0.5F);

    private CoordSet targetSet;

    public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake, Random rand) {
        super(world, player.posX + ((rand.nextDouble() - 0.5F) * 0.5F), player.posY - (rand.nextDouble() * 1.1D), player.posZ + ((rand.nextDouble() - 0.5F) * 0.5F));
        noClip = true;
        particleScale = 0.5F;
        this.targetSet = targetSet;
        particleMaxAge = ticksToTake;
        
        
        motionX = (targetSet.getX() + 0.5F - posX) / ticksToTake;
        motionY = (targetSet.getY() + 0.5F - posY) / ticksToTake;
        motionZ = (targetSet.getZ() + 0.5F - posZ) / ticksToTake;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (this.particleAge++ >= this.particleMaxAge)
            this.setDead();

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public int getFXLayer() {
        return 2;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float tick, float par3, float par4, float par5, float par6, float par7) {
        tessellator.draw();

        glPushMatrix();
        glDepthMask(false);
        glEnable(3042);
        glBlendFunc(770, 771);

        UtilsFX.bindTexture(Strings.DIMENSIONAL_POCKET_PARTICLE);

        COLOUR.doGL();
        float interpX = (float) (prevPosX + (posX - prevPosX) * tick - interpPosX);
        float interpY = (float) (prevPosY + (posY - prevPosY) * tick - interpPosY);
        float interpZ = (float) (prevPosZ + (posZ - prevPosZ) * tick - interpPosZ);

        float tempScale = particleScale * 0.1F;

        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);

        tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 0.5F);
        tessellator.addVertexWithUV(interpX - par3 * tempScale - par6 * tempScale, interpY - par4 * tempScale, interpZ - par5 * tempScale - par7 * tempScale, 0.0D, 1.0D);
        tessellator.addVertexWithUV(interpX - par3 * tempScale + par6 * tempScale, interpY + par4 * tempScale, interpZ - par5 * tempScale + par7 * tempScale, 1.0D, 1.0D);
        tessellator.addVertexWithUV(interpX + par3 * tempScale + par6 * tempScale, interpY + par4 * tempScale, interpZ + par5 * tempScale + par7 * tempScale, 1.0D, 0.0D);
        tessellator.addVertexWithUV(interpX + par3 * tempScale - par6 * tempScale, interpY - par4 * tempScale, interpZ + par5 * tempScale - par7 * tempScale, 0.0D, 0.0D);

        tessellator.draw();

        glDisable(3042);
        glDepthMask(true);

        glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
        tessellator.startDrawingQuads();
    }
}
