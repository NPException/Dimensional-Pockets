package net.gtn.dimensionalpocket.common.core.pocket;

import me.jezza.oc.client.gui.lib.Colour;
import net.minecraft.util.StatCollector;

import java.util.EnumMap;

public enum PocketSideState {
    NONE(Colour.WHITE),
    // used for RF & Items atm.
    ENERGY(Colour.GREEN, "Basic");

    public static PocketSideState[] TEXTURED_STATES = new PocketSideState[]{ENERGY};

    private static EnumMap<PocketSideState, Colour> stateColours;

    static {
        stateColours = new EnumMap<>(PocketSideState.class);
        for (PocketSideState state : values())
            stateColours.put(state, state.colour);
    }

    private Colour colour;
    private String textureName;

    PocketSideState(Colour colour) {
        this(colour, "");
    }

    PocketSideState(Colour colour, String textureName) {
        this.colour = colour.copy();
        this.colour.a = 0.392F;
        this.textureName = textureName;
    }

    public String translateName() {
        return StatCollector.translateToLocal("pocket.side.state." + name());
    }

    public String getTextureName() {
        return textureName;
    }

    public Colour getColour() {
        return colour;
    }

}
