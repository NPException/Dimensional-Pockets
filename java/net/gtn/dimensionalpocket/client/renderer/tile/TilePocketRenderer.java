package net.gtn.dimensionalpocket.client.renderer.tile;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class TilePocketRenderer extends TileEntitySpecialRenderer {
	FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);

	private boolean inRange;
	private static final int planeCount = 16;
	private static final int fieldBrightness = 240;

	private Random random = new Random(31100L);

	private ResourceLocation t1 = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/tunnel.png");
	private ResourceLocation pocketFrame = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket.png");
	private ResourceLocation particleField = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/particleField.png");
	private ResourceLocation reducedParticleField = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/particleField32.png");

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		if (tile instanceof TileDimensionalPocket)
			renderDimensionalPocketAt((TileDimensionalPocket) tile, x, y, z, tick);
	}

	public void renderDimensionalPocketAt(TileDimensionalPocket tile, double x, double y, double z, float f) {
		double maxDistance = 16.0; // distance to block
		this.inRange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) < (maxDistance * maxDistance);
		glPushMatrix();
		glDisable(2912);

		// Y Neg
		drawPlane(0, x, y, z, 0.01F);
		// Y Pos
		drawPlane(1, x, y, z, 0.99F);
		// Z Neg
		drawPlane(2, x, y, z, 0.01F);
		// Z Pos
		drawPlane(3, x, y, z, 0.99F);
		// X Neg
		drawPlane(4, x, y, z, 0.01F);
		// X Pos
		drawPlane(5, x, y, z, 0.99F);

		glDisable(2896);
		bindTexture(pocketFrame);

		// DRAW FRAME
		Tessellator instance = Tessellator.instance;
		instance.startDrawingQuads();
		
		if (tile.getWorldObj() != null) {
			instance.setBrightness(tile.getBlockType().getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord));
		} else {
			instance.setBrightness(220);
		}
		float greyScale = 1.0F;
		instance.setColorRGBA_F(greyScale, greyScale, greyScale, 1.0F);

		// @formatter:off
		// Y Neg
		instance.addVertexWithUV(x, y, z, 1.0D, 1.0D);
		instance.addVertexWithUV(x + 1.0D, y, z, 1.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y, z + 1.0D, 0.0D, 0.0D);
		instance.addVertexWithUV(x, y, z + 1.0D, 0.0D, 1.0D);
		// Y Pos
		instance.addVertexWithUV(x, y + 1.0D, z + 1.0D, 1.0D, 1.0D);
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z + 1.0D, 1.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z, 0.0D, 0.0D);
		instance.addVertexWithUV(x, y + 1.0D, z, 0.0D, 1.0D);
		// Z Neg
		instance.addVertexWithUV(x, y, z, 1.0D, 1.0D);
		instance.addVertexWithUV(x, y + 1.0D, z, 1.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z, 0.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y, z, 0.0D, 1.0D);
		// Z Pos
		instance.addVertexWithUV(x, y + 1.0D, z + 1.0D, 1.0D, 1.0D);
		instance.addVertexWithUV(x, y, z + 1.0D, 1.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y, z + 1.0D, 0.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z + 1.0D, 0.0D, 1.0D);
		// X NEG
		instance.addVertexWithUV(x, y, z, 1.0D, 1.0D);
		instance.addVertexWithUV(x, y, z + 1.0D, 1.0D, 0.0D);
		instance.addVertexWithUV(x, y + 1.0D, z + 1.0D, 0.0D, 0.0D);
		instance.addVertexWithUV(x, y + 1.0D, z, 0.0D, 1.0D);
		// X POS
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z, 1.0D, 1.0D);
		instance.addVertexWithUV(x + 1.0D, y + 1.0D, z + 1.0D, 1.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y, z + 1.0D, 0.0D, 0.0D);
		instance.addVertexWithUV(x + 1.0D, y, z, 0.0D, 1.0D);

		instance.draw();
		// END DRAW FRAME

		glEnable(2912);
		glPopMatrix();

		glPushMatrix();
		glEnable(2896);
		glPopMatrix();
	}

	private void drawPlane(int side, double x, double y, double z, float offset) {
		float dX = (float) TileEntityRendererDispatcher.staticPlayerX;
		float dY = (float) TileEntityRendererDispatcher.staticPlayerY;
		float dZ = (float) TileEntityRendererDispatcher.staticPlayerZ;
		glDisable(2896);
		random.setSeed(31100L);
		if (inRange) {
			switch (side) {
			case 0:
				drawPlaneYNeg(dX, dY, dZ, x, y, z, offset);
				break;
			case 1:
				drawPlaneYPos(dX, dY, dZ, x, y, z, offset);
				break;
			case 2:
				drawPlaneZNeg(dX, dY, dZ, x, y, z, offset);
				break;
			case 3:
				drawPlaneZPos(dX, dY, dZ, x, y, z, offset);
				break;
			case 4:
				drawPlaneXNeg(dX, dY, dZ, x, y, z, offset);
				break;
			case 5:
				drawPlaneXPos(dX, dY, dZ, x, y, z, offset);
				break;
			}
		} else {
			renderOutOfRange(side, x, y, z, offset);
		}

		glDisable(3042);
		glDisable(3168);
		glDisable(3169);
		glDisable(3170);
		glDisable(3171);
		glEnable(2896);
	}

	public void renderOutOfRange(int side, double x, double y, double z, float offset) {
		glPushMatrix();
		bindTexture(reducedParticleField);
		Tessellator instance = Tessellator.instance;
		instance.startDrawingQuads();
		instance.setBrightness(fieldBrightness);
		instance.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);

		// @formatter:off
		switch (side) {
		case 0:
			instance.addVertexWithUV(x + 1.0D, y + offset, z, 1.0D, 1.0D);
			instance.addVertexWithUV(x + 1.0D, y + offset, z + 1.0D, 1.0D, 0.0D);
			instance.addVertexWithUV(x, y + offset, z + 1.0D, 0.0D, 0.0D);
			instance.addVertexWithUV(x, y + offset, z, 0.0D, 1.0D);
			break;
		case 1:
			instance.addVertexWithUV(x, y + offset, z + 1.0D, 1.0D, 1.0D);
			instance.addVertexWithUV(x + 1.0D, y + offset, z + 1.0D, 1.0D, 0.0D);
			instance.addVertexWithUV(x + 1.0D, y + offset, z, 0.0D, 0.0D);
			instance.addVertexWithUV(x, y + offset, z, 0.0D, 1.0D);
			break;
		case 2:
			// Z Neg
			instance.addVertexWithUV(x, y, z + offset, 1.0D, 1.0D);
			instance.addVertexWithUV(x, y + 1.0D, z + offset, 1.0D, 0.0D);
			instance.addVertexWithUV(x + 1.0D, y + 1.0D, z + offset, 0.0D, 0.0D);
			instance.addVertexWithUV(x + 1.0D, y, z + offset, 0.0D, 1.0D);
			break;
		case 3:
			// Z Pos
			instance.addVertexWithUV(x, y + 1.0D, z + offset, 1.0D, 1.0D);
			instance.addVertexWithUV(x, y, z + offset, 1.0D, 0.0D);
			instance.addVertexWithUV(x + 1.0D, y, z + offset, 0.0D, 0.0D);
			instance.addVertexWithUV(x + 1.0D, y + 1.0D, z + offset, 0.0D, 1.0D);
			break;
		case 4:
			// X NEG
			instance.addVertexWithUV(x + offset, y, z, 1.0D, 1.0D);
			instance.addVertexWithUV(x + offset, y, z + 1.0D, 1.0D, 0.0D);
			instance.addVertexWithUV(x + offset, y + 1.0D, z + 1.0D, 0.0D, 0.0D);
			instance.addVertexWithUV(x + offset, y + 1.0D, z, 0.0D, 1.0D);
			break;
		case 5:
			// X POS
			instance.addVertexWithUV(x + offset, y + 1.0D, z, 1.0D, 1.0D);
			instance.addVertexWithUV(x + offset, y + 1.0D, z + 1.0D, 1.0D, 0.0D);
			instance.addVertexWithUV(x + offset, y, z + 1.0D, 0.0D, 0.0D);
			instance.addVertexWithUV(x + offset, y, z, 0.0D, 1.0D);
			break;
		default:
		}
		// @formatter:on

		instance.draw();
		glPopMatrix();
	}

	public void drawPlaneYPos(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(y + offset));
			float f9 = f8 + ActiveRenderInfo.objectY;
			float f10 = f8 + f5 + ActiveRenderInfo.objectY;
			float f11 = f9 / f10;
			f11 += (float) (y + offset);
			glTranslatef(dX, f11, dZ);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dX, -dZ, -dY);
			glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -dY);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x, y + offset, z);
			tessellator1.addVertex(x, y + offset, z + 1.0D);
			tessellator1.addVertex(x + 1.0D, y + offset, z + 1.0D);
			tessellator1.addVertex(x + 1.0D, y + offset, z);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	public void drawPlaneYNeg(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (y + offset);
			float f9 = f8 - ActiveRenderInfo.objectY;
			float f10 = f8 + f5 - ActiveRenderInfo.objectY;
			float f11 = f9 / f10;
			f11 += (float) (y + offset);
			glTranslatef(dX, f11, dZ);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dX, -dZ, -dY);
			glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -dY);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x, y + offset, z + 1.0D);
			tessellator1.addVertex(x, y + offset, z);
			tessellator1.addVertex(x + 1.0D, y + offset, z);
			tessellator1.addVertex(x + 1.0D, y + offset, z + 1.0D);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	public void drawPlaneZPos(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(z + offset));
			float f9 = f8 + ActiveRenderInfo.objectZ;
			float f10 = f8 + f5 + ActiveRenderInfo.objectZ;
			float f11 = f9 / f10;
			f11 += (float) (z + offset);
			glTranslatef(dX, dY, f11);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dX, -dY, -dZ);
			glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dZ);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x, y + 1.0D, z + offset);
			tessellator1.addVertex(x, y, z + offset);
			tessellator1.addVertex(x + 1.0D, y, z + offset);
			tessellator1.addVertex(x + 1.0D, y + 1.0D, z + offset);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	public void drawPlaneZNeg(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (z + offset);
			float f9 = f8 - ActiveRenderInfo.objectZ;
			float f10 = f8 + f5 - ActiveRenderInfo.objectZ;
			float f11 = f9 / f10;
			f11 += (float) (z + offset);
			glTranslatef(dX, dY, f11);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dX, -dY, -dZ);
			glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dZ);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x, y, z + offset);
			tessellator1.addVertex(x, y + 1.0D, z + offset);
			tessellator1.addVertex(x + 1.0D, y + 1.0D, z + offset);
			tessellator1.addVertex(x + 1.0D, y, z + offset);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	public void drawPlaneXPos(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(x + offset));
			float f9 = f8 + ActiveRenderInfo.objectX;
			float f10 = f8 + f5 + ActiveRenderInfo.objectX;
			float f11 = f9 / f10;
			f11 += (float) (x + offset);
			glTranslatef(f11, dY, dZ);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dZ, -dY, -dX);
			glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dX);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x + offset, y + 1.0D, z);
			tessellator1.addVertex(x + offset, y + 1.0D, z + 1.0D);
			tessellator1.addVertex(x + offset, y, z + 1.0D);
			tessellator1.addVertex(x + offset, y, z);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	public void drawPlaneXNeg(float dX, float dY, float dZ, double x, double y, double z, float offset) {
		for (int tessellator = 0; tessellator < planeCount; ++tessellator) {
			glPushMatrix();
			float f5 = 16 - tessellator;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (tessellator == 0) {
				bindTexture(t1);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(3042);
				glBlendFunc(770, 771);
			}
			if (tessellator == 1) {
				bindTexture(particleField);
				glEnable(3042);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (x + offset);
			float f9 = f8 - ActiveRenderInfo.objectX;
			float f10 = f8 + f5 - ActiveRenderInfo.objectX;
			float f11 = f9 / f10;
			f11 += (float) (x + offset);
			glTranslatef(f11, dY, dZ);
			glTexGeni(8192, 9472, 9217);
			glTexGeni(8193, 9472, 9217);
			glTexGeni(8194, 9472, 9217);
			glTexGeni(8195, 9472, 9216);
			glTexGen(8192, 9473, calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			glTexGen(8193, 9473, calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			glTexGen(8194, 9473, calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			glTexGen(8195, 9474, calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			glEnable(3168);
			glEnable(3169);
			glEnable(3170);
			glEnable(3171);
			glPopMatrix();
			glMatrixMode(5890);
			glPushMatrix();
			glLoadIdentity();
			glTranslatef(0.0F, System.currentTimeMillis() % 700000L / 250000.0F, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((tessellator * tessellator * 4321 + tessellator * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslatef(-dZ, -dY, -dX);
			glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dX);
			Tessellator tessellator1 = Tessellator.instance;
			tessellator1.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (tessellator == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator1.setBrightness(fieldBrightness);
			tessellator1.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator1.addVertex(x + offset, y, z);
			tessellator1.addVertex(x + offset, y, z + 1.0D);
			tessellator1.addVertex(x + offset, y + 1.0D, z + 1.0D);
			tessellator1.addVertex(x + offset, y + 1.0D, z);
			// @formatter:on
			tessellator1.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}
	}

	private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
		this.floatBuffer.clear();
		this.floatBuffer.put(f).put(f1).put(f2).put(f3);
		this.floatBuffer.flip();
		return this.floatBuffer;
	}
}
