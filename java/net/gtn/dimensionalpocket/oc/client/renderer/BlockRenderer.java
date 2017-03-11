package net.gtn.dimensionalpocket.oc.client.renderer;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;


@SideOnly(Side.CLIENT)
public class BlockRenderer {
	public static final float OFFSET_1 = 0.0625F;
	public static final float OFFSET_2 = 0.125F;
	public static final float OFFSET_3 = 0.1875F;
	public static final float OFFSET_4 = 0.25F;
	public static final float OFFSET_5 = 0.3125F;
	public static final float OFFSET_6 = 0.375F;
	public static final float OFFSET_7 = 0.4375F;
	public static final float OFFSET_8 = 0.5F;
	public static final float OFFSET_9 = 0.5625F;
	public static final float OFFSET_10 = 0.625F;
	public static final float OFFSET_11 = 0.6875F;
	public static final float OFFSET_12 = 0.75F;
	public static final float OFFSET_13 = 0.8125F;
	public static final float OFFSET_14 = 0.875F;
	public static final float OFFSET_15 = 0.9375F;

	public static final float[] DEFAULT_OFFSETS = new float[6];
	private static Tessellator instance = Tessellator.instance;

	private BlockRenderer() {
	}

	public static void bindTexture(ResourceLocation texture) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}

	public static void drawFaces(ResourceLocation texture) {
		drawFaces(0.0F, 0.0F, 0.0F, texture, DEFAULT_OFFSETS);
	}

	public static void drawFaces(ResourceLocation texture, float scale) {
		drawFaces(0.0F, 0.0F, 0.0F, texture, DEFAULT_OFFSETS, scale);
	}

	public static void drawFaces(ResourceLocation texture, float[] offsets) {
		drawFaces(0.0F, 0.0F, 0.0F, texture, offsets);
	}

	public static void drawFaces(ResourceLocation texture, float[] offsets, float scale) {
		drawFaces(0.0F, 0.0F, 0.0F, texture, offsets, scale);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation texture) {
		drawFaces(x, y, z, new ResourceLocation[] { texture, texture, texture, texture, texture, texture }, DEFAULT_OFFSETS);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation texture, float scale) {
		drawFaces(x, y, z, new ResourceLocation[] { texture, texture, texture, texture, texture, texture }, DEFAULT_OFFSETS, scale);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation texture, float[] offsets) {
		drawFaces(x, y, z, new ResourceLocation[] { texture, texture, texture, texture, texture, texture }, offsets);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation texture, float[] offsets, float scale) {
		drawFaces(x, y, z, new ResourceLocation[] { texture, texture, texture, texture, texture, texture }, offsets, scale);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation[] textures, float[] offsets) {
		drawFaces(x, y, z, textures, offsets, 1.0F);
	}

	public static void drawFaces(double x, double y, double z, ResourceLocation[] textures, float[] offsets, float scale) {
		glPushMatrix();
		glTranslated(x, y, z);

		drawFaceXNeg(textures[0], offsets[0], scale);
		drawFaceXPos(textures[1], offsets[1], scale);
		drawFaceYNeg(textures[2], offsets[2], scale);
		drawFaceYPos(textures[3], offsets[3], scale);
		drawFaceZNeg(textures[4], offsets[4], scale);
		drawFaceZPos(textures[5], offsets[5], scale);

		glPopMatrix();
	}

	public static void drawFace(ForgeDirection direction, ResourceLocation texture, float offset) {
		switch (direction) {
			case DOWN:
				BlockRenderer.drawFaceYNeg(texture, offset);
				break;
			case UP:
				BlockRenderer.drawFaceYPos(texture, offset);
				break;
			case NORTH:
				BlockRenderer.drawFaceZNeg(texture, offset);
				break;
			case SOUTH:
				BlockRenderer.drawFaceZPos(texture, offset);
				break;
			case WEST:
				BlockRenderer.drawFaceXNeg(texture, offset);
				break;
			case EAST:
				BlockRenderer.drawFaceXPos(texture, offset);
				break;
			default:
			case UNKNOWN:
				break;
		}
	}

	public static void drawFace(ForgeDirection direction, ResourceLocation texture, float offset, float scale) {
		switch (direction) {
			case DOWN:
				BlockRenderer.drawFaceYNeg(texture, offset, scale);
				break;
			case UP:
				BlockRenderer.drawFaceYPos(texture, offset, scale);
				break;
			case NORTH:
				BlockRenderer.drawFaceZNeg(texture, offset, scale);
				break;
			case SOUTH:
				BlockRenderer.drawFaceZPos(texture, offset, scale);
				break;
			case WEST:
				BlockRenderer.drawFaceXNeg(texture, offset, scale);
				break;
			case EAST:
				BlockRenderer.drawFaceXPos(texture, offset, scale);
				break;
			default:
			case UNKNOWN:
				break;
		}
	}

	public static void drawFaceXNeg(ResourceLocation texture, float offset) {
		drawFaceXNeg(texture, offset, 1.0F);
	}

	public static void drawFaceXNeg(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(-1.0F, 0.0F, 0.0F);

		// @formatter:off
		instance.addVertexWithUV(0.0D - offset, 0.0D        , 0.0D          , 0.0D, 1.0D);
		instance.addVertexWithUV(0.0D - offset, 0.0D        , 1.0D * scale  , 1.0D, 1.0D);
		instance.addVertexWithUV(0.0D - offset, 1.0D * scale, 1.0D * scale  , 1.0D, 0.0D);
		instance.addVertexWithUV(0.0D - offset, 1.0D * scale, 0.0D          , 0.0D, 0.0D);
		// @formatter:on

		instance.draw();
	}

	public static void drawFaceXPos(ResourceLocation texture, float offset) {
		drawFaceXPos(texture, offset, 1.0F);
	}

	public static void drawFaceXPos(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(1.0F, 0.0F, 0.0F);

		// @formatter:off
		instance.addVertexWithUV(1.0D + offset, 0.0D        , 1.0D * scale  , 0.0D, 1.0D);
		instance.addVertexWithUV(1.0D + offset, 0.0D        , 0.0D          , 1.0D, 1.0D);
		instance.addVertexWithUV(1.0D + offset, 1.0D * scale, 0.0D          , 1.0D, 0.0D);
		instance.addVertexWithUV(1.0D + offset, 1.0D * scale, 1.0D * scale  , 0.0D, 0.0D);
		// @formatter:on

		instance.draw();
	}

	public static void drawFaceYNeg(ResourceLocation texture, float offset) {
		drawFaceYNeg(texture, offset, 1.0F);
	}

	public static void drawFaceYNeg(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(0.0F, -1.0F, 0.0F);

		// @formatter:off
		instance.addVertexWithUV(0.0D           , 0.0D - offset, 0.0D           , 1.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 0.0D - offset, 0.0D           , 0.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 0.0D - offset, 1.0D * scale   , 0.0D, 1.0D);
		instance.addVertexWithUV(0.0D           , 0.0D - offset, 1.0D * scale   , 1.0D, 1.0D);
		// @formatter:on

		instance.draw();
	}

	public static void drawFaceYPos(ResourceLocation texture, float offset) {
		drawFaceYPos(texture, offset, 1.0F);
	}

	public static void drawFaceYPos(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(0.0F, 1.0F, 0.0F);

		// @formatter:off
		instance.addVertexWithUV(0.0D           , 1.0D + offset, 1.0D * scale   , 1.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 1.0D + offset, 1.0D * scale   , 0.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 1.0D + offset, 0.0D           , 0.0D, 1.0D);
		instance.addVertexWithUV(0.0D           , 1.0D + offset, 0.0D           , 1.0D, 1.0D);
		// @formatter:on

		instance.draw();
	}

	public static void drawFaceZNeg(ResourceLocation texture, float offset) {
		drawFaceZNeg(texture, offset, 1.0F);
	}

	public static void drawFaceZNeg(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(0.0F, 0.0F, -1.0F);

		// @formatter:off
		instance.addVertexWithUV(0.0D           , 1.0D * scale  , 0.0D - offset, 1.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 1.0D * scale  , 0.0D - offset, 0.0D, 0.0D);
		instance.addVertexWithUV(1.0D * scale   , 0.0D          , 0.0D - offset, 0.0D, 1.0D);
		instance.addVertexWithUV(0.0D           , 0.0D          , 0.0D - offset, 1.0D, 1.0D);
		// @formatter:on

		instance.draw();
	}

	public static void drawFaceZPos(ResourceLocation texture, float offset) {
		drawFaceZPos(texture, offset, 1.0F);
	}

	public static void drawFaceZPos(ResourceLocation texture, float offset, float scale) {
		bindTexture(texture);
		if (!instance.isDrawing) {
			instance.startDrawingQuads();
		}
		instance.setNormal(0.0F, 0.0F, 1.0F);

		// @formatter:off
		instance.addVertexWithUV(0.0D           , 1.0D * scale  , 1.0D + offset, 0.0D, 0.0D);
		instance.addVertexWithUV(0.0D           , 0.0D          , 1.0D + offset, 0.0D, 1.0D);
		instance.addVertexWithUV(1.0D * scale   , 0.0D          , 1.0D + offset, 1.0D, 1.0D);
		instance.addVertexWithUV(1.0D * scale   , 1.0D * scale  , 1.0D + offset, 1.0D, 0.0D);
		// @formatter:on

		instance.draw();
	}
}
