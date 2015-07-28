package net.gtn.dimensionalpocket.client.particles;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import me.jezza.oc.client.gui.lib.Colour;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.client.utils.UtilsFX;
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

	private static final long frames = 20;

	private long frameOffset;
	private Colour colour;

	public PlayerStreamFX(World world, EntityPlayer player, CoordSet targetSet, int ticksToTake, Random rand, Colour colour) {
		super(world, player.posX + ((rand.nextDouble() - 0.5F) * 2.0F), player.posY + player.height / 4 - (rand.nextDouble() * 1.1D), player.posZ + ((rand.nextDouble() - 0.5F) * 2.0F));
		noClip = true;
		particleScale = 1.5F;
		this.colour = colour.copy();
		particleMaxAge = ticksToTake;

		frameOffset = rand.nextInt((int) frames);

		motionX = (targetSet.x + 0.5F - posX) / ticksToTake;
		motionY = (targetSet.y + 0.5F - posY) / ticksToTake;
		motionZ = (targetSet.z + 0.5F - posZ) / ticksToTake;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		moveEntity(motionX, motionY, motionZ);
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

		double nanosPerFrame = 500000000.0;
		long particleNumber = (long) ((System.nanoTime() % nanosPerFrame) / (nanosPerFrame / frames)) + 1L;
		particleNumber += frameOffset;

		if (particleNumber > frames) {
			particleNumber = particleNumber % frames;
		}

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
