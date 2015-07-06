package net.gtn.dimensionalpocket.client.particles;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.util.Random;

import me.jezza.oc.client.gui.lib.Colour;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * TO BE DECIDED
 * @author Jeremy
 *
 */
public class PlayerExplosionFX extends EntityFX {

    private static final Random random = new Random();

    private int particleNumber;
    private Colour colour;

    protected PlayerExplosionFX(World world, EntityPlayer player, Random rand, Colour colour) {
        super(world, player.posX + ((rand.nextDouble() - 0.5F) * 0.5F), player.posY - (rand.nextDouble() * 1.1D), player.posZ + ((rand.nextDouble() - 0.5F) * 0.5F));
        this.colour = colour.copy();
        
        motionX = (16 - posX) / 10;
        motionY = (16 - posY) / 10;
        motionZ = (16 - posZ) / 10;
        
        particleNumber = random.nextInt(3) + 1;
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

        UtilsFX.bindTexture(Strings.DIMENSIONAL_POCKET_PARTICLE + particleNumber);

        colour.doGLColor4();
        float interpX = (float) (prevPosX + (posX - prevPosX) * tick - interpPosX);
        float interpY = (float) (prevPosY + (posY - prevPosY) * tick - interpPosY);
        float interpZ = (float) (prevPosZ + (posZ - prevPosZ) * tick - interpPosZ);

        float tempScale = particleScale * 0.1F;

        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);

        tessellator.setColorRGBA_F((float) colour.r, (float) colour.g, (float) colour.b, (float) colour.a);
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
