package net.gtn.dimensionalpocket.client.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

    public static void renderItemStackInGUI(ItemStack itemStack, FontRenderer fontRendererObj, RenderItem itemRender, int x, int y, float zLevel) {
        if (itemStack == null)
            return;

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        RenderHelper.enableGUIStandardItemLighting();
        glDisable(GL_LIGHTING);
        glEnable(GL_RESCALE_NORMAL);
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_LIGHTING);

        itemRender.zLevel = zLevel;

        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, textureManager, itemStack, x, y);
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, textureManager, itemStack, x, y);

        itemRender.zLevel = 0.0F;

        glDisable(GL_LIGHTING);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
    }
}
