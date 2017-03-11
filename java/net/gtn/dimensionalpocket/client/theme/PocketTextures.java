package net.gtn.dimensionalpocket.client.theme;

import static net.gtn.dimensionalpocket.client.lib.TextureMaps.TEXTURE_BLOCK_ROOT;
import static net.gtn.dimensionalpocket.common.core.pocket.PocketSideState.TEXTURED_STATES;

import java.util.EnumMap;

import net.gtn.dimensionalpocket.client.lib.IColourBlindTexture;
import net.gtn.dimensionalpocket.common.core.pocket.PocketSideState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;


public final class PocketTextures {
	private PocketTextures() {
		throw new IllegalStateException("No instance for you.");
	}

	public static final ResourceLocation pocketFrame = new ResourceLocation(TEXTURE_BLOCK_ROOT + "pocketFrame.png");
	public static final ResourceLocation pocketFrameInside = new ResourceLocation(TEXTURE_BLOCK_ROOT + "pocketFrameInside.png");
	public static final ResourceLocation connector = new ResourceLocation(TEXTURE_BLOCK_ROOT + "wallConnector.png");
	public static final ResourceLocation connectorBG = new ResourceLocation(TEXTURE_BLOCK_ROOT + "wallConnector_bg.png");

	private static final EnumMap<PocketSideState, IColourBlindTexture> overlays = new EnumMap<>(PocketSideState.class);
	private static final EnumMap<ForgeDirection, IColourBlindTexture> sideIndicators = new EnumMap<>(ForgeDirection.class);

	static {
		// initialize indicators
		ResourceLocation indicator = new ResourceLocation(TEXTURE_BLOCK_ROOT + "pocketIndicator.png");

		for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
			sideIndicators.put(fd.getOpposite(), new TextureWrapper(indicator, new ResourceLocation(TEXTURE_BLOCK_ROOT + "pocketIndicator_" + fd.getOpposite().name().toLowerCase() + ".png")));
		}
		sideIndicators.put(ForgeDirection.UP, new TextureWrapper(indicator));
		sideIndicators.put(ForgeDirection.DOWN, new TextureWrapper(indicator));

		// initialize overlays
		for (PocketSideState state : TEXTURED_STATES) {
			String stateString = TEXTURE_BLOCK_ROOT + "pocketOverlay" + state.getTextureName();
			overlays.put(state, new TextureWrapper(new ResourceLocation(stateString + ".png"), new ResourceLocation(stateString + "CB.png")));
		}
	}

	public static IColourBlindTexture sideIndicatorFor(ForgeDirection direction) {
		return sideIndicators.get(direction);
	}

	public static IColourBlindTexture overlayFor(PocketSideState sideState) {
		return overlays.get(sideState);
	}
}
