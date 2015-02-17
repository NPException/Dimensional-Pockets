package net.gtn.dimensionalpocket.common.items;

import me.jezza.oc.common.items.ItemAbstract;
import net.gtn.dimensionalpocket.DimensionalPockets;
import net.gtn.dimensionalpocket.common.lib.Reference;

public class ItemDP extends ItemAbstract {

    public ItemDP(String name) {
        super(name);
        setCreativeTab(DimensionalPockets.creativeTab);
    }

    @Override
    public String getModIdentifier() {
        return Reference.MOD_IDENTIFIER;
    }
}
