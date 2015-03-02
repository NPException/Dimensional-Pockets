package net.gtn.dimensionalpocket.common.lib;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;
import me.jezza.oc.api.configuration.Config.ConfigInteger;
import net.gtn.dimensionalpocket.client.theme.Theme;

public class Reference {

    public static final String MOD_ID = "dimensionalPockets";
    public static final String MOD_NAME = "Dimensional Pockets";
    public static final String VERSION = "0.10.4";

    public static final String MOD_IDENTIFIER = MOD_ID + ":";

    public static final String CLIENT_PROXY_CLASS = "net.gtn.dimensionalpocket.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "net.gtn.dimensionalpocket.common.CommonProxy";

    public static final String MOD_DOWNLOAD_URL = "http://minecraft.curseforge.com/mc-mods/226990-dimensional-pockets";
    public static final String MOD_CHANGELOG_URL = "https://github.com/NPException42/Dimensional-Pockets/wiki/Changelog";
    public static final String REMOTE_VERSION_FILE = "https://raw.githubusercontent.com/NPException42/Dimensional-Pockets/master/latest_versions.json";

    /*
     * DEBUGGING CONFIGS
     */
    @ConfigBoolean(category = "Debugging", comment = {"If set to \"true\" a RuntimeException will be thrown if there ever",
            "is a client-only method called by the server or vice versa."})
    public static boolean ENFORCE_SIDED_METHODS = false;

    @ConfigBoolean(category = "Debugging", comment = "Set this to \"true\" if you desperately want to try to break your world :P")
    public static boolean CAN_BREAK_POCKET_WALL_IN_CREATIVE = false;

    /*
     * CLIENT PERFORMANCE CONFIGS
     */
    @ConfigInteger(category = "Performance", minValue = 1, maxValue = 50, comment = {"If you experience low FPS, try reducing this number first",
            "before switching fancy rendering off entirely.",
            "Or if you have render power to spare you could raise this value.",
            "(This setting only affects the client)"})
    public static int NUMBER_OF_PARTICLE_PLANES = 15;

    @ConfigBoolean(category = "Performance", comment = {"Set this to false if you experience low FPS due to",
            "the particle field rendering of the Dimensional Pocket.",
            "(This setting only affects the client)"})
    public static boolean USE_FANCY_RENDERING = true; // Minecraft.getMinecraft().gameSettings.fancyGraphics - use this at some point.

    /*
     * GAMEPLAY CONFIGS
     */
    @ConfigInteger(category = "Gameplay")
    public static int DIMENSION_ID = 33;

    @ConfigInteger(category = "Gameplay")
    public static int BIOME_ID = 99;

    @ConfigBoolean(category = "Gameplay", comment = "Decides whether or not any player spawns with a book upon new spawn.")
    public static boolean SHOULD_SPAWN_WITH_BOOK = true;

    @ConfigInteger(category = "Gameplay", minValue = 0, maxValue = 1, comment = "dp.info.theme")
    public static int CURRENT_THEME = 0;

    public static Theme THEME = Theme.DEFAULT;

    /*
     * GENERAL CONFIGS
     */
    @ConfigBoolean(category = "General")
    public static boolean KEEP_POCKET_ROOMS_CHUNK_LOADED = true;

    @ConfigBoolean(category = "General", comment = "If you do not want the mod to check for more recent versions, set this to \"false\".")
    public static boolean DO_VERSION_CHECK = true;

    @ConfigBoolean(category = "General", comment = "If you have a hard time distinguishing colours, you can change this to true.\n"
            + "Gameplay relevant parts of the mod will be displayed in a less color dependent way then.")
    public static boolean COLOR_BLIND_MODE = false;
}
