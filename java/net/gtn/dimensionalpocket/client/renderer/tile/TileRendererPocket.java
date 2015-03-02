package net.gtn.dimensionalpocket.client.renderer.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.lib.Colour;
import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.nio.FloatBuffer;
import java.util.Random;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.TEXTURE_PARTICLE_FIELD_ROOT;
import static net.gtn.dimensionalpocket.common.lib.Reference.THEME;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class TileRendererPocket extends TileEntitySpecialRenderer {

    public static boolean doIndicateSides = false;
    protected FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);

    protected boolean inRange;
    protected float stateColorLevel;
    protected float fieldTranslation;

    private static float maxPlaneDepth = 16f;
    private static float minPlaneDepth = 1f;

    // Equal to Reference.NUMBER_OF_PARTICLE_PLANES + 1 because of the tunnel layer.
    private int planeCount;
    private float planeDepthIncrement;

    private static final int maxBrightness = 240;
    private static final int fieldBrightness = maxBrightness;

    private final Random random = new Random();
    private final long seed = random.nextLong() / 6; // ensure that it will always be multiplicative by the side IDs

    public static final ResourceLocation[] particleFieldTextures = {
            new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleField.png"),
            new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleFieldStatic.png"),
            new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "particleFieldReduced.png")
    };

    protected ResourceLocation currentParticleFieldTexture = particleFieldTextures[0];

    protected static ResourceLocation tunnel = new ResourceLocation(TEXTURE_PARTICLE_FIELD_ROOT + "tunnel.png");

    @Override
    protected void bindTexture(ResourceLocation texture) {
        field_147501_a.field_147553_e.bindTexture(texture);
    }

    public void renderDimensionalPocketAt(TileDimensionalPocket tile, double x, double y, double z, float tick) {
        double maxDistance = 32.0; // distance to block
        this.inRange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) < (maxDistance * maxDistance);

        glPushMatrix();
        glDisable(GL_FOG);

        updateParticleField(2F);

        if (inRange) {
            // Y Neg
            drawParticleField(0, x, y, z, 0.001F, 1.0F);
            // Y Pos
            drawParticleField(1, x, y, z, 0.999F, 1.0F);
            // Z Neg
            drawParticleField(2, x, y, z, 0.001F, 1.0F);
            // Z Pos
            drawParticleField(3, x, y, z, 0.999F, 1.0F);
            // X Neg
            drawParticleField(4, x, y, z, 0.001F, 1.0F);
            // X Pos
            drawParticleField(5, x, y, z, 0.999F, 1.0F);
        } else
            BlockRenderer.drawFaces(x, y, z, currentParticleFieldTexture, new float[]{-0.001F, -0.001F, -0.001F, -0.001F, -0.001F, -0.001F});

        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Tessellator instance = Tessellator.instance;

        instance.startDrawingQuads();
        instance.setBrightness(maxBrightness);

        glTranslated(x, y, z);
        glColor3f(1.0F, 1.0F, 1.0F);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            BlockRenderer.drawFace(direction, THEME.getPocketTexture(), 0F);

        if (!inRange) {
            glDisable(GL_BLEND);
            glEnable(GL_LIGHTING);
            glEnable(GL_FOG);
            glPopMatrix();
            return;
        }

        // Rendering sides
        if (doIndicateSides) {

            Pocket pocket = tile.getPocket();
            updateStateColorLevel();

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                PocketSideState state = pocket.getFlowState(direction);
                IColourBlindTexture texture = THEME.getOverlay(state);
                if (texture == null)
                    continue;
                Colour colour = state.getColour();
                glColor4d(colour.r * stateColorLevel, colour.g * stateColorLevel, colour.b * stateColorLevel, colour.a * stateColorLevel);
                BlockRenderer.drawFace(direction, texture.getTexture(Reference.COLOR_BLIND_MODE), 0.0002F);
            }


            instance.startDrawingQuads();
            instance.setBrightness(maxBrightness);
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                Colour texColour = Utils.FD_COLOURS.get(direction);
                glColor4d(texColour.r, texColour.g, texColour.b, texColour.a);
                BlockRenderer.drawFace(direction, THEME.getSideIndicator(direction).getTexture(Reference.COLOR_BLIND_MODE), 0.0001F);
            }
        }

        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glEnable(GL_FOG);
        glPopMatrix();
    }

    protected void updateStateColorLevel() {
        long colorCycleTime = 1337000000L;
        double minColorLevel = 0.5;
        this.stateColorLevel = (float) (minColorLevel + (1 - minColorLevel) * Math.sin((System.nanoTime() % colorCycleTime) * Math.PI / colorCycleTime));
    }

    /**
     * Set time in ms it should take the particle field to translate once completely
     */
    protected void updateParticleField(float speed) {
        long cycleTime = (long) (250000L / speed);
        this.fieldTranslation = System.currentTimeMillis() % cycleTime / ((float) cycleTime);
        currentParticleFieldTexture = inRange ? Reference.USE_FANCY_RENDERING ? particleFieldTextures[0] : particleFieldTextures[1] : particleFieldTextures[2];
        planeCount = Reference.NUMBER_OF_PARTICLE_PLANES + 1;
        planeDepthIncrement = (maxPlaneDepth - minPlaneDepth) / (planeCount);
    }

    protected void drawParticleField(int side, double x, double y, double z, double offset, double scale) {
        float dX = (float) TileEntityRendererDispatcher.staticPlayerX;
        float dY = (float) TileEntityRendererDispatcher.staticPlayerY;
        float dZ = (float) TileEntityRendererDispatcher.staticPlayerZ;

        glPushMatrix();
        glDisable(GL_LIGHTING);
        random.setSeed(seed * (side + 1)); // ensures different seed per side, but same seed for same side

        switch (side) {
            case 0:
                drawPlaneYNeg(dX, dY, dZ, x, y, z, offset, scale);
                break;
            case 1:
                drawPlaneYPos(dX, dY, dZ, x, y, z, offset, scale);
                break;
            case 2:
                drawPlaneZNeg(dX, dY, dZ, x, y, z, offset, scale);
                break;
            case 3:
                drawPlaneZPos(dX, dY, dZ, x, y, z, offset, scale);
                break;
            case 4:
                drawPlaneXNeg(dX, dY, dZ, x, y, z, offset, scale);
                break;
            case 5:
                drawPlaneXPos(dX, dY, dZ, x, y, z, offset, scale);
                break;
        }

        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_GEN_S);
        glDisable(GL_TEXTURE_GEN_T);
        glDisable(GL_TEXTURE_GEN_R);
        glDisable(GL_TEXTURE_GEN_Q);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void drawPlaneYPos(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dX, -dZ, -dY);
            glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -dY);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private void drawPlaneYNeg(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dX, -dZ, -dY);
            glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -dY);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private void drawPlaneZPos(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dX, -dY, -dZ);
            glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dZ);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private void drawPlaneZNeg(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dX, -dY, -dZ);
            glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dZ);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private void drawPlaneXPos(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dZ, -dY, -dX);
            glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dX);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private void drawPlaneXNeg(float dX, float dY, float dZ, double x, double y, double z, double offset, double scale) {
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
                bindTexture(currentParticleFieldTexture);
                glEnable(GL_BLEND);
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
            glTranslatef(0.0F, fieldTranslation, 0.0F);
            glScalef(f6, f6, f6);
            glTranslatef(0.5F, 0.5F, 0.0F);
            glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            glTranslatef(-0.5F, -0.5F, 0.0F);
            glTranslatef(-dZ, -dY, -dX);
            glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -dX);
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
            tessellator.setBrightness(fieldBrightness);
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
    }

    private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
        this.floatBuffer.clear();
        this.floatBuffer.put(f).put(f1).put(f2).put(f3);
        this.floatBuffer.flip();
        return this.floatBuffer;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        if (tile instanceof TileDimensionalPocket)
            renderDimensionalPocketAt((TileDimensionalPocket) tile, x, y, z, tick);
    }
}
