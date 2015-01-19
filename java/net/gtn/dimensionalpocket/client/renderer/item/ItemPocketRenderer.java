package net.gtn.dimensionalpocket.client.renderer.item;

import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.glTranslatef;

public class ItemPocketRenderer extends BlockRenderer implements IItemRenderer {
    private RenderBlocks rb = new RenderBlocks();
    private Tessellator instance = Tessellator.instance;

    private ResourceLocation pocketFrame = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/dimensionalPocket_frame.png");
    private ResourceLocation reducedParticleField = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/particleField32.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.EQUIPPED_BLOCK;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        if (type == ItemRenderType.INVENTORY) {
            glTranslatef(0.0F, -0.1F, 0.0F);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(pocketFrame);
        instance.startDrawingQuads();

        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
        // Y Pos
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
        // Z Neg
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 0.0D, 1.0D);
        // Z Pos
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 0.0D, 1.0D);
        // X NEG
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
        // X POS
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 0.0D, 1.0D);

        instance.draw();


    }
}
