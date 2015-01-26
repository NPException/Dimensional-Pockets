package net.gtn.dimensionalpocket.common.lib;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import me.jezza.oc.api.configuration.Config.ConfigInteger;

public class Reference {

    public static final String MOD_ID = "dimensionalPockets";
    public static final String MOD_NAME = "Dimensional Pockets";
    public static final String VERSION = "0.10.0";

    public static final String MOD_IDENTIFIER = MOD_ID + ":";

    public static final String CLIENT_PROXY_CLASS = "net.gtn.dimensionalpocket.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "net.gtn.dimensionalpocket.common.CommonProxy";
    
    @ConfigBoolean(category = "Debugging", comment = "If set to \"true\" a RuntimeException will be thrown if there ever\n"
                                                   + "is a client-only method called by the server or vice versa.")
    public static boolean ENFORCE_SIDED_METHODS = false;
    
    @ConfigBoolean(category = "Debugging", comment = "Set this to \"true\" if you desperatly want to try to break your world :P")
    public static boolean CAN_BREAK_POCKET_WALL_IN_CREATIVE = false;

    @ConfigInteger(category = "Gameplay")
    public static int DIMENSION_ID = 33;

    @ConfigInteger(category = "Gameplay")
    public static int BIOME_ID = 99;

    @ConfigBoolean(category = "Gameplay", comment = "Decides whether or not any player spawns with a book upon new spawn.")
    public static boolean SHOULD_SPAWN_WITH_BOOK = true;
}
