package net.gtn.dimensionalpocket.common.lib;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import me.jezza.oc.api.configuration.Config.ConfigInteger;

public class Reference {

    public static final String MOD_ID = "dimensionalPockets";
    public static final String MOD_NAME = "Dimensional Pockets";
    public static final String VERSION = "0.07.7";

    public static final String MOD_IDENTIFIER = MOD_ID + ":";

    public static final String CLIENT_PROXY_CLASS = "net.gtn.dimensionalpocket.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "net.gtn.dimensionalpocket.common.CommonProxy";

    @ConfigInteger(category = "Gameplay")
    public static int DIMENSION_ID = 33;

    @ConfigInteger(category = "Gameplay")
    public static int BIOME_ID = 99;

    @ConfigBoolean(category = "Gameplay", comment = "Decides whether or not any player spawns with a book upon new spawn.")
    public static boolean SHOULD_SPAWN_WITH_BOOK = true;
}
