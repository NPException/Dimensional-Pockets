package net.gtn.dimensionalpocket.common.lib;

import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigBoolean;
import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigFloat;
import net.gtn.dimensionalpocket.oc.api.configuration.Config.ConfigInteger;
import net.minecraft.client.Minecraft;


public class Reference {

	public static final String MOD_ID = "dimensionalPockets";
	public static final String MOD_NAME = "Dimensional Pockets";
	public static final String VERSION = "1.0.1";

	public static final String MOD_IDENTIFIER = MOD_ID + ":";

	public static final String CLIENT_PROXY_CLASS = "net.gtn.dimensionalpocket.client.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "net.gtn.dimensionalpocket.common.CommonProxy";

	public static final String MOD_DOWNLOAD_URL = "http://minecraft.curseforge.com/mc-mods/226990-dimensional-pockets";
	public static final String MOD_CHANGELOG_URL = "https://github.com/NPException/Dimensional-Pockets/wiki/Changelog";
	public static final String REMOTE_VERSION_FILE = "https://raw.githubusercontent.com/NPException/Dimensional-Pockets/master/latest_versions.json";

	/*
	 * DEBUGGING CONFIGS
	 */
	@ConfigBoolean(category = "Debugging", comment = { "If set to \"true\" a RuntimeException will be thrown if there ever",
			"is a client-only method called by the server or vice versa." })
	public static boolean ENFORCE_SIDED_METHODS = false;

	@ConfigBoolean(category = "Debugging", comment = "Set this to \"true\" if you desperately want to try to break your world :P")
	public static boolean CAN_BREAK_POCKET_WALL_IN_CREATIVE = false;

	/*
	 * CLIENT PERFORMANCE CONFIGS
	 */
	@ConfigInteger(category = "Graphics", minValue = 1, maxValue = 50, comment = { "If you experience low FPS, try reducing this number first",
			"before switching fancy rendering off entirely.",
			"Or if you have render power to spare you could raise this value.",
			"(This setting only affects the client. This setting will have no effect if you use the particle field shader)" })
	public static int NUMBER_OF_PARTICLE_PLANES = 15;

	@ConfigInteger(category = "Graphics", minValue = 0, maxValue = 2,
			comment = { "0 = Particle field appearance based on Minecraft's Graphics settings (fancy or fast)",
					"1 = Particle field appearance forced to non-fancy version",
					"2 = Particle field appearance forced to fancy version",
					"(This setting only affects the client)" })
	public static int FORCE_FANCY_RENDERING = 0;

	public static boolean useFancyField() {
		return FORCE_FANCY_RENDERING != 1 && (FORCE_FANCY_RENDERING == 2 || Minecraft.getMinecraft().gameSettings.fancyGraphics);
	}

	@ConfigBoolean(category = "Graphics", comment = "If set to true, a shader will be used for the particle field rendering instead of the old method.")
	public static boolean USE_SHADER_FOR_PARTICLE_FIELD = false;

	/*
	 * GAMEPLAY CONFIGS
	 */
	@ConfigInteger(category = "Gameplay")
	public static int DIMENSION_ID = 33;

	@ConfigInteger(category = "Gameplay")
	public static int BIOME_ID = 99;

	@ConfigFloat(category = "Gameplay", minValue = 0.0F, maxValue = 6000000.0F, comment = "You can change this to modify the resistance of the DP to explosions.")
	public static float DIMENSIONAL_POCKET_RESISTANCE = 15F;

	@ConfigBoolean(category = "Gameplay", comment = "Decides whether or not any player spawns with a book upon new spawn.")
	public static boolean SHOULD_SPAWN_WITH_BOOK = true;

	@ConfigInteger(category = "Gameplay", minValue = 0,
			comment = { "This is the number of Dimensional Pockets you can craft with a pair of crystals before they break.",
					"If set to 0, crystals will never break." })
	public static int CRAFTINGS_PER_CRYSTAL = 4;

	@ConfigBoolean(category = "Gameplay", comment = "Decides if the Nether Crystal and End Crystal can be crafted without hostile mob drops.\n"
			+ "The Nether Crystal will use gold nuggets instead of Ghast tears, and the End Crystal will use Quartz instead of Ender Eyes")
	public static boolean USE_PEACEFUL_RECIPES = false;

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

	/*
	 * ANALYTICS
	 */
	@ConfigBoolean(category = "Analytics",
			comment = { "If this is set to true AND you have snooper enabled in Minecraft, the mod will collect anonymous usage data.",
					"For example how much RF and Fluids pass through Pockets and how many players get trapped." })
	public static boolean MAY_COLLECT_ANONYMOUS_USAGE_DATA = true;
}
