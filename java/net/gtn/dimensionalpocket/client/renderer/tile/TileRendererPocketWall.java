package net.gtn.dimensionalpocket.client.renderer.tile;

import static org.lwjgl.opengl.GL11.*;
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
    private static ResourceLocation wallConnector = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket_wall_connector.png");
    
    public TileRendererPocketWall() {
        inRange = true;
        // enable overlay for NONE state
        //overlays.put(PocketSideState.NONE, basicOverlay);
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
        
        int ox = (offX == 0) ? 0 : offX-1;
        int oy = (offY == 0) ? 0 : offY-1;
        int oz = (offZ == 0) ? 0 : offZ-1;
        
        double offset = (wallVisibleSide.ordinal() % 2 == 0) ? 0.001 : 0.999;
        updateFieldTranslation(3F);
        //drawPlane(wallVisibleSide.ordinal(), x-ox, y-oy, z-oz, offset, 14.0);
        drawPlane(wallVisibleSide.ordinal(), x-offX, y-offY, z-offZ, offset, 16.0);
        
        glDisable(GL_LIGHTING);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        Pocket pocket = (tile == null) ? null : tile.getPocket();
        
        renderFaceOnWall(wallVisibleSide, x-ox, y-oy, z-oz, 0.001d, 14.0, pocket, innerPocketFrame);
        renderFaceOnWall(wallVisibleSide, x-offX, y-offY, z-offZ, 0.001d, 16.0, pocket, null);
        
        renderFaceOnWall(wallVisibleSide, x, y, z, 0.0015d, 1.0, pocket, wallConnector);
        updateStateColorLevel();

        glDisable(GL_BLEND);

        glEnable(GL_LIGHTING);

        glEnable(GL_FOG);
        
        glPopMatrix();
    }

    private void renderFaceOnWall(ForgeDirection side, double x, double y, double z, double offset, double scale, Pocket pocket, ResourceLocation texture) {
        Tessellator instance = Tessellator.instance;

        // @formatter:off
		// Y Neg
        if (side == ForgeDirection.DOWN && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y - offset, z          , 1.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y - offset, z          , 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y - offset, z + scale  , 0.0D, 0.0D);
    		instance.addVertexWithUV(x          , y - offset, z + scale  , 0.0D, 1.0D);
    		instance.draw();
        }
		
		// Y Pos
        if (side == ForgeDirection.UP && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + 1.0D + offset, z + scale, 1.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y + 1.0D + offset, z + scale, 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y + 1.0D + offset, z        , 0.0D, 0.0D);
    		instance.addVertexWithUV(x          , y + 1.0D + offset, z        , 0.0D, 1.0D);
    		instance.draw();
        }
		
		// Z Neg
        if (side == ForgeDirection.NORTH && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + scale , z - offset, 0.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y + scale , z - offset, 1.0D, 1.0D);
    		instance.addVertexWithUV(x + scale  , y         , z - offset, 1.0D, 0.0D);
    		instance.addVertexWithUV(x          , y         , z - offset, 0.0D, 0.0D);
    		instance.draw();
        }
		
		// Z Pos
        if (side == ForgeDirection.SOUTH && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x          , y + scale , z + 1.0D + offset, 1.0D, 1.0D);
    		instance.addVertexWithUV(x          , y         , z + 1.0D + offset, 1.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y         , z + 1.0D + offset, 0.0D, 0.0D);
    		instance.addVertexWithUV(x + scale  , y + scale , z + 1.0D + offset, 0.0D, 1.0D);
    		instance.draw();
        }
		
		// X Neg
        if (side == ForgeDirection.WEST && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x - offset, y        , z          , 1.0D, 0.0D);
    		instance.addVertexWithUV(x - offset, y        , z + scale  , 0.0D, 0.0D);
    		instance.addVertexWithUV(x - offset, y + scale, z + scale  , 0.0D, 1.0D);
    		instance.addVertexWithUV(x - offset, y + scale, z          , 1.0D, 1.0D);
    		instance.draw();
        }
		
		// X Pos
        if (side == ForgeDirection.EAST && prepareRenderForSide(texture, side.getOpposite(), pocket, instance)) {
    		instance.addVertexWithUV(x + 1.0D + offset, y         , z + scale  , 1.0D, 0.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y         , z          , 0.0D, 0.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y + scale , z          , 0.0D, 1.0D);
    		instance.addVertexWithUV(x + 1.0D + offset, y + scale , z + scale  , 1.0D, 1.0D);
    		instance.draw();
        }
		
		// @formatter:on
    }
}
