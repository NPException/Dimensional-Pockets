package net.gtn.dimensionalpocket.client.renderer.item;

import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

public class ItemPocketRenderer extends BlockRenderer implements IItemRenderer {
    private static float inverseOf255 = 0.00392156862F;

    private Tessellator instance = Tessellator.instance;

    private ResourceLocation pocketFrame = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket.png");
    private ResourceLocation pocketOverlay = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket_overlay.png");
    private ResourceLocation particleField = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/misc/particleField.png");

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
        glPushMatrix();
        glDisable(2896);

        boolean inventoryFlag = type == ItemRenderType.INVENTORY;

        if (inventoryFlag) {
            glTranslatef(0.0F, -0.1F, 0.0F);
            glEnable(2896);
        }

        renderCube(pocketFrame);

        glPushMatrix();
        glTranslatef(0.005F, 0.005F, 0.005F);
        glScalef(0.98F, 0.98F, 0.98F);
        renderCube(particleField);
        glPopMatrix();

        if (!inventoryFlag) {
            glEnable(3042);
            glBlendFunc(770, 771);
        }

        long l = System.currentTimeMillis() % 0xFFFFFF;
        float red = (l & 0xFF0000L) >> 16;
        float green = (l & 0xFF00L) >> 8;
        float blue = (l & 0xFFL);

        glColor3f(red * inverseOf255, green * inverseOf255, blue * inverseOf255);
        renderCube(pocketOverlay);

        glDisable(3042);
        glEnable(2896);
        glPopMatrix();
    }

    private void renderCube(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        instance.startDrawingQuads();

//        Y Neg
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
//        Y Pos
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
//        Z Neg
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
//        Z Pos
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 0.0D, 1.0D);
//        X Neg
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 1.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 1.0D, 0.0D, 1.0D, 1.0D);
//        X Pos
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0D, 1.0D);

        instance.draw();
    }
}
