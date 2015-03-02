package net.gtn.dimensionalpocket.client.particles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.lib.Colour;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class PlayerStreamFX extends EntityFX {

    private static final Random random = new Random();

    private int particleNumber;
    private Colour colour;

    public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake, Random rand, Colour colour) {
        super(world, player.posX + ((rand.nextDouble() - 0.5F) * 0.6F), player.posY - (rand.nextDouble() * 1.1D), player.posZ + ((rand.nextDouble() - 0.5F) * 0.6F));
        noClip = true;
        particleScale = 0.5F;
        this.colour = colour.copy();
        particleMaxAge = ticksToTake;

        motionX = (targetSet.x + 0.5F - posX) / ticksToTake;
        motionY = (targetSet.y + 0.5F - posY) / ticksToTake;
        motionZ = (targetSet.z + 0.5F - posZ) / ticksToTake;
        
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
