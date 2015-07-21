/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import net.gtn.dimensionalpocket.DimensionalPockets;
import net.minecraft.client.Minecraft;


/**
 * @author NPException
 *
 */
public class DPCrashAnalyzer {
	private static final String LAST_ANALYZED_FILE = "lastAnalyzedCrashReport";

	/**
	 * Analyzes the crashlog the most recent crashlog file for any involvement of
	 * DP in the crash and returns the content for the error event, or null if
	 * nothing needs to be sent.
	 *
	 * @param analyticsConfig
	 * @param isClient
	 * @return
	 * @throws IOException
	 */
	public static String analyzeCrash(Properties analyticsConfig, boolean isClient) throws IOException {
		String lastAnalyzed = analyticsConfig.getProperty(LAST_ANALYZED_FILE);
		File mostRecent = null;
		if (isClient) {
			File crashfolder = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
			File[] crashLogs = crashfolder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith("crash-") && name.endsWith("-client.txt");
				}
			});
			for (File log : crashLogs) {
				if (mostRecent == null || log.getName().compareTo(mostRecent.getName()) > 0) {
					mostRecent = log;
				}
			}
		} else {
			// TODO: grab most recent crash file on server
		}

		if (mostRecent.getName().equals(lastAnalyzed))
			return null;

		byte[] encoded = Files.readAllBytes(Paths.get(mostRecent.getPath()));
		String content = new String(encoded, "UTF-8");

		if (!content.contains("at " + DimensionalPockets.class.getPackage().getName()))
			return null; // DP basepackage not found in crashlog, so it is probably not our fault.

		analyticsConfig.setProperty(LAST_ANALYZED_FILE, mostRecent.getName());
		return content;
	}
}
