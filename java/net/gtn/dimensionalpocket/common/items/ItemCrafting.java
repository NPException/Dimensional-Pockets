package net.gtn.dimensionalpocket.common.items;

import net.gtn.dimensionalpocket.common.items.framework.ItemDPMeta;

public class ItemCrafting extends ItemDPMeta {

    private static final String[] names = new String[] { "enderCrystal", "netherCrystal" };

    public ItemCrafting(String name) {
        super(name);
    }

    @Override
    public String[] getNames() {
        return names;
    }

}
