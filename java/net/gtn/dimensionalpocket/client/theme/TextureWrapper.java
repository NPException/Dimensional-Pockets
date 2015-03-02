package net.gtn.dimensionalpocket.client.theme;

import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.minecraft.util.ResourceLocation;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.BLANK_TEXTURE;

public class TextureWrapper implements IColourBlindTexture {

    private ResourceLocation defaultTexture, colourBlindTexture;

    public TextureWrapper() {
        this.defaultTexture = BLANK_TEXTURE;
        this.colourBlindTexture = BLANK_TEXTURE;
    }

    public TextureWrapper(ResourceLocation defaultTexture) {
        this.defaultTexture = defaultTexture != null ? defaultTexture : BLANK_TEXTURE;
        this.colourBlindTexture = BLANK_TEXTURE;
    }

    public TextureWrapper(ResourceLocation defaultTexture, ResourceLocation colourBlindTexture) {
        this.defaultTexture = defaultTexture != null ? defaultTexture : BLANK_TEXTURE;
        this.colourBlindTexture = colourBlindTexture != null ? colourBlindTexture : BLANK_TEXTURE;
    }

    @Override
    public ResourceLocation getTexture(boolean colourBlind) {
        return colourBlind ? colourBlindTexture : defaultTexture;
    }
}
