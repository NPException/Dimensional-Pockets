package net.gtn.dimensionalpocket.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

    public static void renderItemStackInGUI(ItemStack itemStack, FontRenderer fontRendererObj, RenderItem itemRender, int x, int y, float zLevel) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);

        itemRender.zLevel = zLevel;

        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, textureManager, itemStack, x, y);
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, textureManager, itemStack, x, y);

        itemRender.zLevel = 0.0F;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

}
