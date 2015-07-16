package net.gtn.dimensionalpocket.client.utils.version;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.jezza.oc.common.utils.Localise;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.core.utils.Utils;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.Loader;


public class VersionChecker {

	/**
	 * Adds the current version to a version map file<br>
	 * NPE uses "${resource_loc:/Dimensional Pockets/resources}" as working
	 * directory for a VersionChecker RunConfiguration
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Filepath to the version map file needed");
			return;
		}

		String fileContent = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(line);
			}
			fileContent = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Map<String, Version> versionMap = getVersionMap(fileContent);
		if (versionMap == null) {
			versionMap = new HashMap<>();
		} else {
			versionMap = new HashMap<>(versionMap);
		}

		Version version = new Version(Reference.VERSION, Reference.MOD_DOWNLOAD_URL, Reference.MOD_CHANGELOG_URL, "Go get it!", EnumChatFormatting.AQUA.name());

		versionMap.put("1.7.10", version);

		try (FileWriter writer = new FileWriter(args[0])) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Type type = new TypeToken<Map<String, Version>>() {/**/
			}.getType();
			gson.toJson(versionMap, type, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Version> getVersionMap(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Version>>() {/**/
		}.getType();

		Map<String, Version> versionMap = null;

		try {
			versionMap = gson.fromJson(json, type);
		} catch (Exception ex) {
			DPLogger.warning("Could not read recent version from json map", VersionChecker.class);
		}

		return versionMap;
	}

	// TODO implement this someday
	//    /**
	//     * This is an integration with Dynious Version Checker See
	//     * http://www.minecraftforum.net/topic/2721902-
	//     */
	//    public static void sendIMCOutdatedMessage(Version latest) {
	//        if (Loader.isModLoaded("VersionChecker")) {
	//            NBTTagCompound compound = new NBTTagCompound();
	//            compound.setString("modDisplayName", Reference.MOD_NAME);
	//            compound.setString("oldVersion", Reference.VERSION);
	//            compound.setString("newVersion", latest.version);
	//
	//            compound.setString("updateUrl", latest.url);
	//            compound.setBoolean("isDirectLink", false);
	//
	//            FMLInterModComms.sendRuntimeMessage("BuildCraft|Core", "VersionChecker", "addUpdate", compound);
	//            sentIMCOutdatedMessage = true;
	//        }
	//    }

	public static Version getLatestVersion() {
		try {
			String location = Reference.REMOTE_VERSION_FILE;
			HttpURLConnection conn = null;
			while (location != null && !location.isEmpty()) {
				URL url = new URL(location);

				if (conn != null) {
					conn.disconnect();
				}

				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 6.0; ru; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 (.NET CLR 3.5.30729)");
				conn.connect();
				location = conn.getHeaderField("Location");
			}

			StringBuilder content = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (content.length() > 0) {
						content.append("\n");
					}
					content.append(line);
				}

				conn.disconnect();
			}

			String mcVersion = Loader.instance().getMinecraftModContainer().getVersion();

			Map<String, Version> versionMap = getVersionMap(content.toString());

			if (versionMap != null) {
				Version remoteLatest = versionMap.get(mcVersion);
				if (remoteLatest != null) {
					if (remoteLatest.isNewerThan(new Version(Reference.VERSION, Reference.MOD_DOWNLOAD_URL, Reference.MOD_CHANGELOG_URL, null, null)))
						return remoteLatest;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void checkUpToDate(EntityPlayer player) {
		Version latest = getLatestVersion();
		if (latest != null) {
			player.addChatMessage(new ChatComponentText(Localise.format("info.update.available.1", latest.version)));
			player.addChatMessage(new ChatComponentText(Localise.format("info.update.available.2", Reference.VERSION)));

			if (latest.additionalInfo != null) {
				ChatComponentText info = new ChatComponentText(latest.additionalInfo);

				if (latest.additionalInfoColour != null) {
					EnumChatFormatting ecf = Utils.getColourByName(latest.additionalInfoColour.toUpperCase());
					info.getChatStyle().setColor(ecf);
				}
				player.addChatMessage(info);
			}

			IChatComponent linkLine = new ChatComponentText("[ ");
			linkLine.appendSibling(Utils.createChatLink("DOWNLOAD", latest.url, true, false, false, EnumChatFormatting.AQUA));
			linkLine.appendSibling(new ChatComponentText(" ]"));

			if (latest.changelog != null) {
				linkLine.appendSibling(new ChatComponentText(" - [ "));
				linkLine.appendSibling(Utils.createChatLink("CHANGELOG", latest.changelog, false, false, false, EnumChatFormatting.DARK_AQUA));
				linkLine.appendSibling(new ChatComponentText(" ]"));
			}

			player.addChatMessage(linkLine);
		}
	}
}
