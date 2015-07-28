package net.gtn.dimensionalpocket.client.renderer;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.*;
import static net.gtn.dimensionalpocket.common.lib.Hacks.BlockRenderer.*;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.Random;

import net.gtn.dimensionalpocket.client.event.ClientEventHandler;
import net.gtn.dimensionalpocket.common.lib.Hacks;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;


public class PortalRenderer {

	public static final ResourceLocation[] fieldTextures = {
		new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleField.png"),
			new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleFieldStaticSmall.png"),
			new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleFieldStatic.png")
	};

	public static final int MAX_BRIGHTNESS = 240;
	public static final PortalRenderer instance = new PortalRenderer();

	protected static ResourceLocation tunnel = new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "tunnel.png");

	protected ResourceLocation currentTexture = fieldTextures[0];
	protected FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
	protected Random random = new Random();

	protected boolean isDrawing, inRange, fancyRendering;
	protected double x, y, z;
	protected float maxPlaneDepth, minPlaneDepth, fieldTranslation, planeDepthIncrement;
	protected int planeCount, brightness;

	private PortalRenderer() {
		resetRenderer();
	}

	public void resetRenderer() {
		x = y = z = 0.0D;
		isDrawing = inRange = false;
		fancyRendering = true;
		// 16 planes and 1 for the tunnel layer.
		planeCount = 17;

		maxPlaneDepth = 16F;
		minPlaneDepth = 1F;
		brightness = MAX_BRIGHTNESS;
	}

	public void overrideRange(boolean inRange) {
		this.inRange = inRange;
	}

	public void overrideMaxPlaneDepth(float planeDepth) {
		maxPlaneDepth = planeDepth;
	}

	/**
	 * One is added for the tunnel layer.
	 *
	 * @param planeCount The number of planes to render.
	 */
	public void overridePlaneCount(int planeCount) {
		this.planeCount = planeCount + 1;
	}

	public void overrideFancyRendering(boolean fancyRendering) {
		this.fancyRendering = fancyRendering;
	}

	public void setInterpolatedPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public boolean isDrawing() {
		return isDrawing;
	}

	public void setSeed(long seed) {
		random.setSeed(seed);
	}

	public void startDrawing() {
		if (isDrawing)
			throw new IllegalStateException("Already drawing!");

		glPushMatrix();
		isDrawing = true;
	}

	public void stopDrawing() {
		if (!isDrawing)
			throw new IllegalStateException("Can't stop renderer! It's not drawing!");

		resetRenderer();
		glPopMatrix();
	}

	public void updateField(boolean small) {
		long cycleTime = (long) (5000L / (small ? 2F : 3F));
		fieldTranslation = ClientEventHandler.gameTicks % cycleTime / ((float) cycleTime);
		currentTexture = fancyRendering ? fieldTextures[0] : fieldTextures[small ? 1 : 2];
		planeDepthIncrement = (maxPlaneDepth - minPlaneDepth) / (planeCount);
	}

	public void drawFaces(float offset) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		drawFaceYNeg(offset);
		drawFaceYPos(offset);
		drawFaceZNeg(offset);
		drawFaceZPos(offset);
		drawFaceXNeg(offset);
		drawFaceXPos(offset);
	}

	public void drawFaces(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		drawFaceYNeg(offset, scale);
		drawFaceYPos(offset, scale);
		drawFaceZNeg(offset, scale);
		drawFaceZPos(offset, scale);
		drawFaceXNeg(offset, scale);
		drawFaceXPos(offset, scale);
	}

	public void drawFace(ForgeDirection direction, float offset) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");
		switch (direction) {
			case DOWN:
				drawFaceYNeg(offset);
				break;
			case UP:
				drawFaceYPos(offset);
				break;
			case NORTH:
				drawFaceZNeg(offset);
				break;
			case SOUTH:
				drawFaceZPos(offset);
				break;
			case WEST:
				drawFaceXNeg(offset);
				break;
			case EAST:
				drawFaceXPos(offset);
				break;
			default:
			case UNKNOWN:
				break;
		}
	}

	public void drawFace(ForgeDirection direction, float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");
		switch (direction) {
			case DOWN:
				drawFaceYNeg(offset, scale);
				break;
			case UP:
				drawFaceYPos(offset, scale);
				break;
			case NORTH:
				drawFaceZNeg(offset, scale);
				break;
			case SOUTH:
				drawFaceZPos(offset, scale);
				break;
			case WEST:
				drawFaceXNeg(offset, scale);
				break;
			case EAST:
				drawFaceXPos(offset, scale);
				break;
			default:
			case UNKNOWN:
				break;
		}
	}

	public void drawFaceXNeg(float offset) {
		drawFaceXNeg(offset, 1.0F);
	}

	public void drawFaceXNeg(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");
		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceXNeg(currentTexture, -offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (x + offset);
			float f9 = f8 - ActiveRenderInfo.objectX;
			float f10 = f8 + f5 - ActiveRenderInfo.objectX;
			float f11 = f9 / f10;
			f11 += (float) (x + offset);
			glTranslated(f11, staticPlayerY, staticPlayerZ);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerZ, -staticPlayerY, -staticPlayerX);
			glTranslated(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -staticPlayerX);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x + offset, y,         z);
			tessellator.addVertex(x + offset, y,         z + scale);
			tessellator.addVertex(x + offset, y + scale, z + scale);
			tessellator.addVertex(x + offset, y + scale, z);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	public void drawFaceXPos(float offset) {
		drawFaceXPos(offset, 1.0F);
	}

	public void drawFaceXPos(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceXPos(currentTexture, -1 + offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(x + offset));
			float f9 = f8 + ActiveRenderInfo.objectX;
			float f10 = f8 + f5 + ActiveRenderInfo.objectX;
			float f11 = f9 / f10;
			f11 += (float) (x + offset);
			glTranslated(f11, staticPlayerY, staticPlayerZ);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerZ, -staticPlayerY, -staticPlayerX);
			glTranslated(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -staticPlayerX);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x + offset, y + scale, z);
			tessellator.addVertex(x + offset, y + scale, z + scale);
			tessellator.addVertex(x + offset, y,         z + scale);
			tessellator.addVertex(x + offset, y,         z);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	public void drawFaceYNeg(float offset) {
		drawFaceYNeg(offset, 1.0F);
	}

	public void drawFaceYNeg(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceYNeg(currentTexture, -offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (y + offset);
			float f9 = f8 - ActiveRenderInfo.objectY;
			float f10 = f8 + f5 - ActiveRenderInfo.objectY;
			float f11 = f9 / f10;
			f11 += (float) (y + offset);
			glTranslated(staticPlayerX, f11, staticPlayerZ);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerX, -staticPlayerZ, -staticPlayerY);
			glTranslated(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -staticPlayerY);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x,         y + offset, z + scale);
			tessellator.addVertex(x,         y + offset, z       );
			tessellator.addVertex(x + scale, y + offset, z       );
			tessellator.addVertex(x + scale, y + offset, z + scale);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	public void drawFaceYPos(float offset) {
		drawFaceYPos(offset, 1.0F);
	}

	public void drawFaceYPos(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceYPos(currentTexture, -1 + offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(y + offset));
			float f9 = f8 + ActiveRenderInfo.objectY;
			float f10 = f8 + f5 + ActiveRenderInfo.objectY;
			float f11 = f9 / f10;
			f11 += (float) (y + offset);
			glTranslated(staticPlayerX, f11, staticPlayerZ);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerX, -staticPlayerZ, -staticPlayerY);
			glTranslated(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -staticPlayerY);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x,         y + offset, z);
			tessellator.addVertex(x,         y + offset, z + scale);
			tessellator.addVertex(x + scale, y + offset, z + scale);
			tessellator.addVertex(x + scale, y + offset, z);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	public void drawFaceZNeg(float offset) {
		drawFaceZNeg(offset, 1.0F);
	}

	public void drawFaceZNeg(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceZNeg(currentTexture, -offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (z + offset);
			float f9 = f8 - ActiveRenderInfo.objectZ;
			float f10 = f8 + f5 - ActiveRenderInfo.objectZ;
			float f11 = f9 / f10;
			f11 += (float) (z + offset);
			glTranslated(staticPlayerX, staticPlayerY, f11);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerX, -staticPlayerY, -staticPlayerZ);
			glTranslated(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -staticPlayerZ);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x,         y,         z + offset);
			tessellator.addVertex(x,         y + scale, z + offset);
			tessellator.addVertex(x + scale, y + scale, z + offset);
			tessellator.addVertex(x + scale, y,         z + offset);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	public void drawFaceZPos(float offset) {
		drawFaceZPos(offset, 1.0F);
	}

	public void drawFaceZPos(float offset, float scale) {
		if (!isDrawing)
			throw new IllegalStateException("Renderer is not drawing!");

		glPushMatrix();
		glDisable(GL_LIGHTING);

		if (!fancyRendering) {
			glTranslated(x, y, z);
			Hacks.BlockRenderer.drawFaceZPos(currentTexture, -1 + offset, scale);
			glPopMatrix();
			return;
		}

		int i = -1;
		for (float depthDecrease = 0f; i < planeCount; depthDecrease += planeDepthIncrement) {
			i++;
			glPushMatrix();
			float f5 = maxPlaneDepth - depthDecrease;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);
			if (i == 0) {
				bindTexture(tunnel);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			if (i == 1) {
				bindTexture(currentTexture);
				glEnable(GL_BLEND);
				glBlendFunc(1, 1);
				f6 = 0.5F;
			}
			float f8 = (float) (-(z + offset));
			float f9 = f8 + ActiveRenderInfo.objectZ;
			float f10 = f8 + f5 + ActiveRenderInfo.objectZ;
			float f11 = f9 / f10;
			f11 += (float) (z + offset);
			glTranslated(staticPlayerX, staticPlayerY, f11);
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
			glTranslatef(0.0F, fieldTranslation, 0.0F);
			glScalef(f6, f6, f6);
			glTranslatef(0.5F, 0.5F, 0.0F);
			glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			glTranslatef(-0.5F, -0.5F, 0.0F);
			glTranslated(-staticPlayerX, -staticPlayerY, -staticPlayerZ);
			glTranslated(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -staticPlayerZ);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			f11 = random.nextFloat() * 0.5F + 0.1F;
			float f12 = random.nextFloat() * 0.5F + 0.4F;
			float f13 = random.nextFloat() * 0.5F + 0.5F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			tessellator.setBrightness(brightness);
			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			// @formatter:off
			tessellator.addVertex(x,         y + scale, z + offset);
			tessellator.addVertex(x,         y,         z + offset);
			tessellator.addVertex(x + scale, y,         z + offset);
			tessellator.addVertex(x + scale, y + scale, z + offset);
			// @formatter:on
			tessellator.draw();
			glPopMatrix();
			glMatrixMode(5888);
		}

		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
		floatBuffer.clear();
		floatBuffer.put(f).put(f1).put(f2).put(f3);
		floatBuffer.flip();
		return floatBuffer;
	}
}
