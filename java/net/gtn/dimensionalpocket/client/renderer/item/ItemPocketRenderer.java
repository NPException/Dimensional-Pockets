package net.gtn.dimensionalpocket.client.renderer.item;

import me.jezza.oc.client.renderer.BlockRenderer;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPocketRenderer extends BlockRenderer implements IItemRenderer {

    private TileRendererPocket tileRenderer = new TileRendererPocket();
    
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
    }
}
