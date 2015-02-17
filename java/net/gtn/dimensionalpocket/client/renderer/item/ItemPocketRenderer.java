package net.gtn.dimensionalpocket.client.renderer.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPushMatrix;

@SideOnly(Side.CLIENT)
public class ItemPocketRenderer extends BlockRenderer implements IItemRenderer {

    private static ResourceLocation overlay = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dp_overlay_basic.png");
    private static ResourceLocation frameTexture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/dimensionalPocket.png");

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
        
        if (type == ItemRenderType.INVENTORY)
            glTranslatef(0.0F, -0.1F, 0.0F);
        if (type == ItemRenderType.ENTITY)
            glTranslatef(-0.5F, -0.4F, -0.5F);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        drawFaces(0.0D, 0.0D, 0.0D, frameTexture);
        glDisable(GL_BLEND);
        
        glPopMatrix();
    }
}
