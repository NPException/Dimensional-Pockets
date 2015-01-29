package net.gtn.dimensionalpocket.client.renderer.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezza.oc.client.gui.lib.Colour;
import me.jezza.oc.common.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocketWallConnector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileRendererPocketWall extends TileRendererPocket {
    
    private static ResourceLocation innerPocketFrame = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocketInside.png");
    private static ResourceLocation wallConnector = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dp_wall_connector.png");
    private static ResourceLocation wallConnectorBackground = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dp_wall_connector_bg.png");
    
    private Colour connectorBaseColor = Colour.WHITE.copy();
    private Colour connectorColor = connectorBaseColor.copy();
    private Colour connectorBGColour = Colour.WHITE.copy();
    
    {
        connectorBGColour.a = 0.80;
    }
    
    private void updateConnectorColor() {
        long colorCycleTime = 5000000000L;
        double minColorLevel = 0.5;
        double level = (minColorLevel + (1-minColorLevel) * Math.sin((System.nanoTime()%colorCycleTime) * Math.PI / colorCycleTime));
        
        connectorColor.r = connectorBaseColor.r * level;
        connectorColor.g = connectorBaseColor.g * level;
        connectorColor.b = connectorBaseColor.b * level;
        connectorColor.a = connectorBaseColor.a * level;
    }
    
    public TileRendererPocketWall() {
        inRange = true;
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        if (tile instanceof TileDimensionalPocketWallConnector)
            renderDimensionalPocketWallAt((TileDimensionalPocketWallConnector) tile, x, y, z, tick);
    }

    @Override
    protected void bindTexture(ResourceLocation texture) {
        super.bindTexture(texture);
    }

    /**
     * Method is used by tile and item renderer.
     * Last three arguments are passed by the item renderer.
     * if itemStack is null (and tile is not null) it is rendering a tile,
     * otherwise it is rendering an item
     */
    private void renderDimensionalPocketWallAt(TileDimensionalPocketWallConnector tile, double x, double y, double z, float f) {
        glPushMatrix();

        glDisable(GL_FOG);
        
        CoordSet offsetCoords = tile.getCoordSet().asChunkOffset();
        ForgeDirection wallVisibleSide = Pocket.getSideForBlock(offsetCoords).getOpposite();
        
        // %15 is to ensure that only the two coordinates that build the plane of the wall are non zero
        int offX = offsetCoords.getX() % 15;
        int offY = offsetCoords.getY() % 15;
        int offZ = offsetCoords.getZ() % 15;
        
        double offset = (wallVisibleSide.ordinal() % 2 == 0) ? 0.001 : 0.999;
        updateFieldTranslation(3F);
        drawParticleField(wallVisibleSide.ordinal(), x-offX, y-offY, z-offZ, offset, 16.0);
        
        glDisable(GL_LIGHTING);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        Pocket pocket = (tile == null) ? null : tile.getPocket();
        
        // frame other offsets and scale because the texture is smaller
        int ox = (offX == 0) ? 0 : offX-1;
        int oy = (offY == 0) ? 0 : offY-1;
        int oz = (offZ == 0) ? 0 : offZ-1;
        renderFaceOnWall(wallVisibleSide, x-ox, y-oy, z-oz, 0.001d, 14.0, pocket, Colour.WHITE, innerPocketFrame);
        
        // corners
        if (doIndicateSides) {
            renderFaceOnWall(wallVisibleSide, x-offX, y-offY, z-offZ, 0.0015, 16.0, pocket, null, sideIndicators.get(wallVisibleSide));
            if (Reference.COLOR_BLIND_MODE) {
                renderFaceOnWall(wallVisibleSide, x-offX, y-offY, z-offZ, 0.0025, 16.0, pocket, Colour.WHITE, colorblindSideIndicators.get(wallVisibleSide.getOpposite()));
            }
        }
        
        // overlays
        updateStateColorLevel();
        renderFaceOnWall(wallVisibleSide, x-offX, y-offY, z-offZ, 0.0020, 16.0, pocket, null, null);
        
        // connector
        updateConnectorColor();
        renderFaceOnWall(wallVisibleSide, x, y, z, 0.0030, 1.0, pocket, connectorBGColour, wallConnectorBackground);
        renderFaceOnWall(wallVisibleSide, x, y, z, 0.0035, 1.0, pocket, connectorColor, wallConnector);

        glDisable(GL_BLEND);

        glEnable(GL_LIGHTING);

        glEnable(GL_FOG);
        
        glPopMatrix();
    }

    private void renderFaceOnWall(ForgeDirection side, double x, double y, double z, double offset, double scale,
            Pocket pocket, Colour colour, ResourceLocation texture) {
        
        Tessellator instance = Tessellator.instance;

        // @formatter:off
		// Y Neg
        if (side == ForgeDirection.DOWN && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y - offset, z          , 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y - offset, z          , 0.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y - offset, z + scale  , 0.0D, 1.0D);
    		instance.addVertexWithUV(x          , y - offset, z + scale  , 1.0D, 1.0D);
    		instance.draw();
        }
		
		// Y Pos
        if (side == ForgeDirection.UP && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + 1.0D + offset, z + scale, 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y + 1.0D + offset, z + scale, 0.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y + 1.0D + offset, z        , 0.0D, 1.0D);
    		instance.addVertexWithUV(x          , y + 1.0D + offset, z        , 1.0D, 1.0D);
    		instance.draw();
        }
		
		// Z Neg
        if (side == ForgeDirection.NORTH && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + scale , z - offset, 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y + scale , z - offset, 0.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y         , z - offset, 0.0D, 1.0D);
    		instance.addVertexWithUV(x          , y         , z - offset, 1.0D, 1.0D);
    		instance.draw();
        }
		
		// Z Pos
        if (side == ForgeDirection.SOUTH && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + scale , z + 1.0D + offset, 0.0D, 0.0D);
    		instance.addVertexWithUV(x          , y         , z + 1.0D + offset, 0.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y         , z + 1.0D + offset, 1.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y + scale , z + 1.0D + offset, 1.0D, 0.0D);
    		instance.draw();
        }
		
		// X Neg
        if (side == ForgeDirection.WEST && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x - offset, y        , z          , 0.0D, 1.0D);
    		instance.addVertexWithUV(x - offset, y        , z + scale  , 1.0D, 1.0D);
    		instance.addVertexWithUV(x - offset, y + scale, z + scale  , 1.0D, 0.0D);
    		instance.addVertexWithUV(x - offset, y + scale, z          , 0.0D, 0.0D);
    		instance.draw();
        }
		
		// X Pos
        if (side == ForgeDirection.EAST && prepareRenderForSide(texture, colour, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x + 1.0D + offset, y         , z + scale  , 0.0D, 1.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y         , z          , 1.0D, 1.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y + scale , z          , 1.0D, 0.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y + scale , z + scale  , 0.0D, 0.0D);
    		instance.draw();
        }
		
		// @formatter:on
    }
}
