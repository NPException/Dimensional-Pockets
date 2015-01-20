package net.gtn.dimensionalpocket.client.renderer.item;

import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.client.renderer.tile.TilePocketRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPocketRenderer extends BlockRenderer implements IItemRenderer {

//    private Tessellator instance = Tessellator.instance;
//
//    private ResourceLocation pocketFrame = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket2.png");
//    private ResourceLocation pocketOverlay = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket_overlay2.png");
//    private ResourceLocation particleField = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/particleField32.png");
    
    private TilePocketRenderer tileRenderer = new TilePocketRenderer();
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    	tileRenderer.renderDimensionalPocketAt(null, 0f, 0f, 0f, 0f, item, type, data);

//    	boolean inventoryFlag = type == ItemRenderType.INVENTORY;
//    	
//    	if (!inventoryFlag) {
//    		tileRenderer.renderDimensionalPocketAt(null, 0f, 0f, 0f, 0f, item, type, data);
//    		return;
//    	}
//    	
//        glPushMatrix();
//        glDisable(GL_LIGHTING);
//
//        if (inventoryFlag) {
//            glTranslatef(0.0F, -0.1F, 0.0F);
//        }
//        
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        
//        //glPushMatrix();
//        glTranslatef(0.005F, 0.005F, 0.005F);
//        glScalef(0.99F, 0.99F, 0.99F);
//        renderCube(particleField);
//        //glPopMatrix();
//
//        renderCube(pocketFrame);
//
//        //renderCube(pocketOverlay);
//
//        glDisable(GL_BLEND);
//        glEnable(GL_LIGHTING);
//        glPopMatrix();
    }

//    private void renderCube(ResourceLocation texture) {
//        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
//        
//        if (texture == pocketOverlay) {
//        	FlowState state = FlowState.NONE;
//            instance.setColorRGBA(state.r, state.g, state.b, state.a);
//        } else {
//        	instance.setColorRGBA(255, 255, 255, 255);
//        }
//
//        instance.startDrawingQuads();
//
////        Y Neg
//        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
//        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
//        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
////        Y Pos
//        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 0.0D);
//        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
////        Z Neg
//        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 1.0D, 1.0D);
//        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
////        Z Pos
//        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
//        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 0.0D, 1.0D);
////        X Neg
//        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
//        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 0.0D, 1.0D);
//        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 1.0D, 1.0D);
////        X Pos
//        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 1.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 0.0D, 0.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 1.0D);
//        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 1.0D);
//
//        instance.draw();
//    }
}
