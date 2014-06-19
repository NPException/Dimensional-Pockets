package net.gtn.dimensionalpocket.common.core.config.comms;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.gtn.dimensionalpocket.common.core.config.comms.framework.AbstractConfig;
import net.gtn.dimensionalpocket.common.lib.Reference;

public class TinkersConstructConfig extends AbstractConfig {

    private static final String clazzName = "tconstruct.util.config.DimensionBlacklist";

    public TinkersConstructConfig(String modID) {
        super(modID);
    }

    @Override
    public void runModSpecificComms() {
        try {
            // Trust me, I tried other ways.
            // I hate myself just as much.
            Class clazz = Class.forName(clazzName);
            Field field = clazz.getField("blacklistedDims");

            ArrayList<Integer> temp = new ArrayList<Integer>();
            ArrayList<Integer> blackListedDims = (ArrayList<Integer>) field.get(temp);

            blackListedDims.add(Reference.DIMENSION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInterModComms() {
    }

}
