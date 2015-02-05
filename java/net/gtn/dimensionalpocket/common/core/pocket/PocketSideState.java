package net.gtn.dimensionalpocket.common.core.pocket;

import net.minecraft.util.StatCollector;

public enum PocketSideState {
    NONE,
    ENERGY; // used for RF & Items atm.
    
    public String translateName() {
        return StatCollector.translateToLocal("pocket.side.state." + name());
    }
}
