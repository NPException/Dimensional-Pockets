package net.gtn.dimensionalpocket.common.lib;

public class Reference {

    public static final String MOD_ID = "dimensionalPockets";
    public static final String MOD_NAME = "Dimensional Pockets";
    public static final String VERSION = "0.07";

    public static final String MOD_IDENTIFIER = MOD_ID + ":";

    public static final String CLIENT_PROXY_CLASS = "net.gtn.dimensionalpocket.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "net.gtn.dimensionalpocket.common.CommonProxy";

    public static final int DIMENSION_ID_DEFAULT = 33;
    public static int DIMENSION_ID;

    public static final int BIOME_ID_DEFAULT = 99;
    public static int BIOME_ID;

    public static final int SIDE_STATE_COUNT = 1;

    public static boolean SHOULD_SPAWN_WITH_BOOK;
}
