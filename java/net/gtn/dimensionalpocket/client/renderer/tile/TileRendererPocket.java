package net.gtn.dimensionalpocket.client.renderer.tile;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import me.jezza.oc.client.gui.lib.Colour;
import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.client.renderer.PortalRenderer;
import net.gtn.dimensionalpocket.client.renderer.shader.ParticleFieldShader;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Hacks;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileRendererPocket extends TileEntitySpecialRenderer {

    public static final int maxBrightness = 240;

    protected PortalRenderer portalRenderer = PortalRenderer.instance;

    public static boolean doIndicateSides = false;

    private static final Random random = new Random();
    public static final long[] seeds = new long[6]; // ensure that it will always be multiplicative by the side IDs

    static {
        long base = random.nextLong() / 6;
        for (int i = 1; i <= 6; i++)
            seeds[i - 1] = base * i;
    }
    
    private static float [] fieldOffsets = new float[6];
    static {
        for(int i=0; i<6; i++)
            fieldOffsets[i] = -0.0001f;
    }

    protected float stateColorLevel;
    private boolean inRange = false;

    @Override
    protected void bindTexture(ResourceLocation texture) {
        field_147501_a.field_147553_e.bindTexture(texture);
    }

    public void renderDimensionalPocketAt(TileDimensionalPocket tile, double x, double y, double z, float tick) {
        double maxDistance = 32.0; // distance to block
        this.inRange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) < (maxDistance * maxDistance);

        boolean fancy = Reference.useFancyField();
        boolean useFieldShader = Reference.USE_SHADER_FOR_PARTICLE_FIELD && fancy && inRange;
        
        if (!useFieldShader) {
            portalRenderer.setInterpolatedPosition(x, y, z);
            portalRenderer.overrideFancyRendering(fancy);
            portalRenderer.overridePlaneCount(Reference.NUMBER_OF_PARTICLE_PLANES);
            portalRenderer.overrideRange(inRange);

            portalRenderer.startDrawing();
            portalRenderer.updateField(2F);

            portalRenderer.setSeed(seeds[0]);
            portalRenderer.drawFaceYNeg(0.001F);
            portalRenderer.setSeed(seeds[1]);
            portalRenderer.drawFaceYPos(0.999F);
            portalRenderer.setSeed(seeds[2]);
            portalRenderer.drawFaceZNeg(0.001F);
            portalRenderer.setSeed(seeds[3]);
            portalRenderer.drawFaceZPos(0.999F);
            portalRenderer.setSeed(seeds[4]);
            portalRenderer.drawFaceXNeg(0.001F);
            portalRenderer.setSeed(seeds[5]);
            portalRenderer.drawFaceXPos(0.999F);

            portalRenderer.stopDrawing();
        }

        glPushMatrix();
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Tessellator instance = Tessellator.instance;

        instance.startDrawingQuads();
        instance.setBrightness(maxBrightness);

        glTranslated(x, y, z);
        glColor3f(1.0F, 1.0F, 1.0F);

        if (useFieldShader) {
            ParticleFieldShader.use();
            Hacks.BlockRenderer.drawFaces(PortalRenderer.fieldTextures[0], fieldOffsets);
            ParticleFieldShader.release();
        }
        Hacks.BlockRenderer.drawFaces(Reference.THEME.getPocketTexture());

        if (!inRange) {
            glDisable(GL_BLEND);
            glEnable(GL_LIGHTING);
            glEnable(GL_FOG);
            glPopMatrix();
            return;
        }

//        Rendering sides
        if (doIndicateSides) {

            instance.startDrawingQuads();
            instance.setBrightness(maxBrightness);
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                Colour texColour = Utils.FD_COLOURS.get(direction);
                glColor4d(texColour.r, texColour.g, texColour.b, texColour.a);
                Hacks.BlockRenderer.drawFace(direction, Reference.THEME.getSideIndicator(direction).getTexture(false), 0.0003F);
                if (Reference.COLOR_BLIND_MODE) {
                    texColour = Colour.WHITE;
                    glColor4d(texColour.r, texColour.g, texColour.b, texColour.a);
                    Hacks.BlockRenderer.drawFace(direction, Reference.THEME.getSideIndicator(direction).getTexture(true), 0.0003F);
                }
            }

            Pocket pocket = tile.getPocket();
            updateStateColorLevel();

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                PocketSideState state = pocket.getFlowState(direction);
                IColourBlindTexture texture = Reference.THEME.getOverlay(state);
                if (texture == null)
                    continue;
                Colour colour = state.getColour();
                glColor4d(colour.r * stateColorLevel, colour.g * stateColorLevel, colour.b * stateColorLevel, colour.a * stateColorLevel);
                Hacks.BlockRenderer.drawFace(direction, texture.getTexture(Reference.COLOR_BLIND_MODE), 0.0002F);
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

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        if (tile instanceof TileDimensionalPocket)
            renderDimensionalPocketAt((TileDimensionalPocket) tile, x, y, z, tick);
    }
}
