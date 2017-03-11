package net.gtn.dimensionalpocket.client.renderer.item;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.client.renderer.PortalRenderer;
import net.gtn.dimensionalpocket.client.theme.PocketTextures;
import net.gtn.dimensionalpocket.oc.client.renderer.BlockRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;


@SideOnly(Side.CLIENT)
public class ItemPocketRenderer implements IItemRenderer {

	private static final float[] offsets = new float[] { -0.001F, -0.001F, -0.001F, -0.001F, -0.001F, -0.001F };

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

		if (type == ItemRenderType.INVENTORY) {
			glTranslatef(0.0F, -0.1F, 0.0F);
		}

		if (type == ItemRenderType.ENTITY) {
			glTranslatef(-0.5F, -0.4F, -0.5F);
		}

		glDisable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1.0F, 1.0F, 1.0F);

		BlockRenderer.drawFaces(PortalRenderer.fieldTextures[1], offsets);
		BlockRenderer.drawFaces(PocketTextures.pocketFrame);

		glDisable(GL_BLEND);
		glEnable(GL_LIGHTING);

		glPopMatrix();
	}
}
