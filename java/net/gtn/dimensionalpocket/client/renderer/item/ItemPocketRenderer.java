package net.gtn.dimensionalpocket.client.renderer.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.client.renderer.PortalRenderer;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class ItemPocketRenderer implements IItemRenderer {

    private static final float[] offsets = new float[]{-0.001F, -0.001F, -0.001F, -0.001F, -0.001F, -0.001F};

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

        BlockRenderer.drawFaces(PortalRenderer.fieldTextures[1], offsets);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        BlockRenderer.drawFaces(Reference.THEME.getPocketTexture());
        glDisable(GL_BLEND);
        glPopMatrix();
    }
}
