package net.gtn.dimensionalpocket.common.core.pocket;

import net.minecraft.util.StatCollector;

public enum PocketSideState {
    NONE,
    ENERGY;
    
    public String translateName() {
        return StatCollector.translateToLocal("pocket.side.state." + name());
    }
}
