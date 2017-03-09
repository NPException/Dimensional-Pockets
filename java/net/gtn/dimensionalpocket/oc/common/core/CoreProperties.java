package net.gtn.dimensionalpocket.oc.common.core;

public class CoreProperties {

    public static final String MOD_ID = "OmnisCore";
    public static final String MOD_NAME = "Omnis Core";
    public static final String VERSION = "0.0.7";

    private static final String BUILD = "1217";

    public static final String FML_REQ = "7.10.84." + BUILD;
    public static final String FML_REQ_MAX = "7.11";

    public static final String FORGE_REQ = "10.13.1." + BUILD;
    public static final String FORGE_REQ_MAX = "10.14";

    public static final String DEPENDENCIES = "required-after:FML@[" + FML_REQ + "," + FML_REQ_MAX + ");" + "required-after:Forge@[" + FORGE_REQ + "," + FORGE_REQ_MAX + ");";

    public static final String SERVER_PROXY = "net.gtn.dimensionalpocket.oc.common.CommonProxy";
    public static final String CLIENT_PROXY = "net.gtn.dimensionalpocket.oc.client.ClientProxy";
}
