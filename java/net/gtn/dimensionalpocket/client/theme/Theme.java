package net.gtn.dimensionalpocket.client.theme;

import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumMap;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.TEXTURE_BLOCK_ROOT;
import static net.gtn.dimensionalpocket.common.core.pocket.PocketSideState.TEXTURED_STATES;

public enum Theme {
    // Add new themes here.
    DEFAULT, METAL;

    public static final int SIZE = Theme.values().length - 1;

    private boolean init = false;

    private ResourceLocation pocketFrame, pocketFrameInside, connector, connectorBG;

    private EnumMap<PocketSideState, IColourBlindTexture> overlays = new EnumMap<>(PocketSideState.class);
    private EnumMap<ForgeDirection, IColourBlindTexture> sideIndicators = new EnumMap<>(ForgeDirection.class);

    Theme() {
    }

    public void init() {
        if (init)
            return;
        init = true;

        String blockDir = TEXTURE_BLOCK_ROOT + name().toLowerCase() + "/";

        pocketFrame = new ResourceLocation(blockDir + "pocketFrame.png");
        pocketFrameInside = new ResourceLocation(blockDir + "pocketFrameInside.png");

        ResourceLocation indicator = new ResourceLocation(blockDir + "pocketIndicator.png");

        // Frame textures and indicators
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
            sideIndicators.put(fd.getOpposite(), new TextureWrapper(indicator, new ResourceLocation(blockDir + "pocketIndicator_" + fd.getOpposite().name().toLowerCase() + ".png")));

        sideIndicators.put(ForgeDirection.UP, new TextureWrapper(indicator));
        sideIndicators.put(ForgeDirection.DOWN, new TextureWrapper(indicator));

        for (PocketSideState state : TEXTURED_STATES) {
            String stateString = blockDir + "pocketOverlay" + state.getTextureName();
            overlays.put(state, new TextureWrapper(new ResourceLocation(stateString + ".png"), new ResourceLocation(stateString + "CB.png")));
        }

        connector = new ResourceLocation(blockDir + "wallConnector.png");
        connectorBG = new ResourceLocation(blockDir + "wallConnector_bg.png");
    }

    public IColourBlindTexture getSideIndicator(ForgeDirection direction) {
        if (!init)
            init();
        return sideIndicators.get(direction);
    }

    public IColourBlindTexture getOverlay(PocketSideState sideState) {
        if (!init)
            init();
        return overlays.get(sideState);
    }

    public ResourceLocation getPocketTexture() {
        if (!init)
            init();
        return pocketFrame;
    }

    public ResourceLocation getPocketInsideTexture() {
        if (!init)
            init();
        return pocketFrameInside;
    }

    public ResourceLocation getConnector() {
        if (!init)
            init();
        return connector;
    }

    public ResourceLocation getConnectorBG() {
        if (!init)
            init();
        return connectorBG;
    }
}
