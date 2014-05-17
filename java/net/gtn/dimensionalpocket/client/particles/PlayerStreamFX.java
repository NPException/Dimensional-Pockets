package net.gtn.dimensionalpocket.client.particles;

import static org.lwjgl.opengl.GL11.*;

import net.gtn.dimensionalpocket.client.utils.Colour;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerStreamFX extends EntityFX {

    private static final Colour colour = new Colour(1.0F, 1.0F, 1.0F, 0.5F);

    public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake) {
        super(world, player.posX, player.posY, player.posZ);
        noClip = true;
        particleMaxAge = ticksToTake;

        motionX = (targetSet.getX() - player.posX) / ticksToTake;
        motionY = (targetSet.getY() - player.posY) / ticksToTake;
        motionZ = (targetSet.getZ() - player.posZ) / ticksToTake;
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

        colour.doGL();

        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);

        float interpX = (float) (prevPosX + (posX - prevPosX) * tick - interpPosX);
        float interpY = (float) (prevPosY + (posY - prevPosY) * tick - interpPosY);
        float interpZ = (float) (prevPosZ + (posZ - prevPosZ) * tick - interpPosZ);

        tessellator.draw();

        glPopMatrix();

        tessellator.startDrawingQuads();
    }
}
