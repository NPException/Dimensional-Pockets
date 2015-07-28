package net.gtn.dimensionalpocket.client.theme;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.*;
import static net.gtn.dimensionalpocket.common.core.pocket.PocketSideState.*;

import java.util.EnumMap;

import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;


public enum Theme {
	// Add new themes here.
	DEFAULT, METAL;

	public static final int SIZE = Theme.values().length - 1;

	private boolean isInitialized = false;

	private ResourceLocation pocketFrame, pocketFrameInside, connector, connectorBG;

	private EnumMap<PocketSideState, IColourBlindTexture> overlays = new EnumMap<>(PocketSideState.class);
	private EnumMap<ForgeDirection, IColourBlindTexture> sideIndicators = new EnumMap<>(ForgeDirection.class);

	Theme() {
	}

	public synchronized void init() {
		if (isInitialized)
			return;
		isInitialized = true;

		String blockDir = TEXTURE_BLOCK_ROOT + name().toLowerCase() + "/";

		pocketFrame = new ResourceLocation(blockDir + "pocketFrame.png");
		pocketFrameInside = new ResourceLocation(blockDir + "pocketFrameInside.png");

		ResourceLocation indicator = new ResourceLocation(blockDir + "pocketIndicator.png");

		// Frame textures and indicators
		for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
			sideIndicators.put(fd.getOpposite(), new TextureWrapper(indicator, new ResourceLocation(blockDir + "pocketIndicator_" + fd.getOpposite().name().toLowerCase() + ".png")));
		}

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
		if (!isInitialized) {
			init();
		}
		return sideIndicators.get(direction);
	}

	public IColourBlindTexture getOverlay(PocketSideState sideState) {
		if (!isInitialized) {
			init();
		}
		return overlays.get(sideState);
	}

	public ResourceLocation getPocketTexture() {
		if (!isInitialized) {
			init();
		}
		return pocketFrame;
	}

	public ResourceLocation getPocketInsideTexture() {
		if (!isInitialized) {
			init();
		}
		return pocketFrameInside;
	}

	public ResourceLocation getConnector() {
		if (!isInitialized) {
			init();
		}
		return connector;
	}

	public ResourceLocation getConnectorBG() {
		if (!isInitialized) {
			init();
		}
		return connectorBG;
	}
}
