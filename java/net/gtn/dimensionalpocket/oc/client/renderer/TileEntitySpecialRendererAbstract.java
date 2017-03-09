package net.gtn.dimensionalpocket.oc.client.renderer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class TileEntitySpecialRendererAbstract<T extends TileEntity> extends TileEntitySpecialRenderer {
    protected TileEntityRendererDispatcher dispatcher;

    @SuppressWarnings("unchecked")
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        renderTileAt((T) tileEntity, x, y, z, tick);
    }

    public abstract void renderTileAt(T tile, double x, double y, double z, float tick);

    @Override
    protected void bindTexture(ResourceLocation texture) {
        field_147501_a.field_147553_e.bindTexture(texture);
    }

    public void func_147497_a(TileEntityRendererDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void func_147496_a(World world) {
        setupRenderer(world);
    }

    public void setupRenderer(World world) {
    }

    public FontRenderer getFontRenderer() {
        return func_147498_b();
    }
}
