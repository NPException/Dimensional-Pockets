package net.gtn.dimensionalpocket.client.renderer.tile;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.client.event.RenderEventHandler;
import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.client.renderer.PortalRenderer;
import net.gtn.dimensionalpocket.client.renderer.shader.ParticleFieldShader;
import net.gtn.dimensionalpocket.client.theme.PocketTextures;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.gtn.dimensionalpocket.oc.client.lib.Colour;
import net.gtn.dimensionalpocket.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.oc.common.utils.CoordSet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;


@SideOnly(Side.CLIENT)
public class TileRendererPocketWall extends TileRendererPocket {

	private Colour connectorBaseColor, connectorColor, connectorBGColour;

	{
		connectorBaseColor = Colour.WHITE.copy();
		connectorColor = Colour.WHITE.copy();
		connectorBGColour = Colour.WHITE.copy();
		connectorBGColour.a = 0.80;
	}

	public TileRendererPocketWall() {
	}

	private void updateConnectorColor(boolean isInGUI) {
		long colorCycleTime = 5000000000L;
		double minColorLevel = 0.5;
		double level = minColorLevel + (1 - minColorLevel) * Math.sin(System.nanoTime() % colorCycleTime * Math.PI / colorCycleTime);

		connectorColor.r = connectorBaseColor.r * level;
		connectorColor.g = connectorBaseColor.g * level;
		connectorColor.b = connectorBaseColor.b * level;
		connectorColor.a = connectorBaseColor.a * level;

		connectorBGColour.a = isInGUI ? 0.2 : 0.8;
	}

	/**
	 * Method is used by tile and item renderer. Last three arguments are passed
	 * by the item renderer. if itemStack is null (and tile is not null) it is
	 * rendering a tile, otherwise it is rendering an item
	 */
	private void renderDimensionalPocketWallAt(TileDimensionalPocketWallConnector tile, double x, double y, double z, float f) {
		CoordSet offsetCoords = tile.getCoordSet().asChunkOffset();
		ForgeDirection wallVisibleSide = Pocket.getSideForConnector(offsetCoords).getOpposite();

		// % 15 is to ensure that only the two coordinates that build the plane of the wall are non zero
		int offX = offsetCoords.x % 15;
		int offY = offsetCoords.y % 15;
		int offZ = offsetCoords.z % 15;

		int ordinal = wallVisibleSide.ordinal();
		float offset = ordinal % 2 == 0 ? 0.0F : 1.0F;

		int tempX = offX;
		int tempY = offY;
		int tempZ = offZ;

		switch (ordinal) {
			case 0:
			case 1:
				tempX -= 1;
				tempZ -= 1;
				break;
			case 2:
			case 3:
				tempX -= 1;
				tempY -= 1;
				break;
			case 4:
			case 5:
				tempY -= 1;
				tempZ -= 1;
				break;
			default:
				return;
		}

		glPushMatrix();

		boolean isInGUI = RenderEventHandler.isRenderingGUI;
		boolean fancy = Reference.useFancyField();
		boolean useFieldShader = Reference.USE_SHADER_FOR_PARTICLE_FIELD && fancy;

		if (!useFieldShader && !isInGUI) {
			portalRenderer.overrideFancyRendering(fancy);
			portalRenderer.overridePlaneCount(Reference.NUMBER_OF_PARTICLE_PLANES);

			portalRenderer.startDrawing();
			portalRenderer.updateField(false);
			portalRenderer.setSeed(seeds[ordinal]);
			portalRenderer.setInterpolatedPosition(x - tempX, y - tempY, z - tempZ);
			portalRenderer.drawFace(wallVisibleSide, offset, 14.0F);
			portalRenderer.stopDrawing();
		}

		glDisable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// frame other offsets and scale because the texture is smaller
		int ox = offX == 0 ? 0 : offX - 1;
		int oy = offY == 0 ? 0 : offY - 1;
		int oz = offZ == 0 ? 0 : offZ - 1;

		glTranslated(x, y, z);

		if (!isInGUI) {
			glPushMatrix();
			glTranslatef(-ox, -oy, -oz);

			if (useFieldShader) {
				ParticleFieldShader.use();
				BlockRenderer.drawFace(wallVisibleSide, PortalRenderer.fieldTextures[0], 0.0F, 14F);
				ParticleFieldShader.release();
			}

			BlockRenderer.drawFace(wallVisibleSide, PocketTextures.pocketFrameInside, 0.001F, 14F);

			glPopMatrix();
		}

		// Connectors
		updateConnectorColor(isInGUI);

		glDisable(GL_CULL_FACE);
		connectorColor.doGLColor4();
		BlockRenderer.drawFace(wallVisibleSide, PocketTextures.connector, 0.005F - (isInGUI ? 0.1f : 0f));

		connectorBGColour.doGLColor4();
		BlockRenderer.drawFace(wallVisibleSide, PocketTextures.connectorBG, 0.004F - (isInGUI ? 0.1f : 0f));
		glEnable(GL_CULL_FACE);

		glTranslatef(-offX, -offY, -offZ);

		if (doIndicateSides) {
			// Indicators
			Colour texColour = Utils.FD_COLOURS.get(wallVisibleSide.getOpposite());
			glColor4d(texColour.r, texColour.g, texColour.b, texColour.a);
			BlockRenderer.drawFace(wallVisibleSide, PocketTextures.sideIndicatorFor(wallVisibleSide).getTexture(false), 0.002F, 16);
			if (Reference.COLOR_BLIND_MODE) {
				texColour = Colour.WHITE;
				glColor4d(texColour.r, texColour.g, texColour.b, texColour.a);
				BlockRenderer.drawFace(wallVisibleSide, PocketTextures.sideIndicatorFor(wallVisibleSide).getTexture(true), 0.003F, 16);
			}

			updateStateColorLevel();

			// Overlays
			Pocket pocket = tile.getPocket();
			if (pocket != null) {
				PocketSideState state = pocket.getFlowState(wallVisibleSide.getOpposite());
				IColourBlindTexture texture = PocketTextures.overlayFor(state);
				if (texture != null) {
					Colour colour = state.getColour();
					glColor4d(colour.r * stateColorLevel, colour.g * stateColorLevel, colour.b * stateColorLevel, colour.a * stateColorLevel);
					BlockRenderer.drawFace(wallVisibleSide, texture.getTexture(Reference.COLOR_BLIND_MODE), 0.001F, 16);
				}
			}
		}

		glDisable(GL_BLEND);
		glEnable(GL_LIGHTING);

		glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		if (tile instanceof TileDimensionalPocketWallConnector) {
			renderDimensionalPocketWallAt((TileDimensionalPocketWallConnector) tile, x, y, z, tick);
		}
	}
}
