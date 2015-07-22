/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import java.io.File;
import java.io.FileFilter;
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
	 * nothing needs to be sent.<br>
	 * A non null return value signals the calling DPAnalytics to save the
	 * analyticsConfig to the hard drive.
	 *
	 * @param analyticsConfig
	 * @param isClient
	 * @return
	 * @throws IOException
	 */
	public static String analyzeCrash(Properties analyticsConfig, final boolean isClient) throws IOException {
		String lastAnalyzed = analyticsConfig.getProperty(LAST_ANALYZED_FILE);

		File crashfolder = new File((isClient) ? Minecraft.getMinecraft().mcDataDir : new File("."), "crash-reports");
		File[] crashLogs = crashfolder.listFiles(new FileFilter() {
			// only check files that are not older than a week 7L * 24L *
			private final long compareTimeStamp = System.currentTimeMillis() - (60L * 60L * 1000L);
			@Override
			public boolean accept(File file) {
				if (file.lastModified() < compareTimeStamp)
					return false;

				String name = file.getName();
				return name.startsWith("crash-") && name.endsWith(isClient ? "-client.txt" : "-server.txt");
			}
		});

		if (crashLogs == null) {
			DPLogger.info("crashlogs was null, crash folder \"" + crashfolder.getAbsolutePath() + "\" probably just doesn't exist. YET. :D");
			return null;
		}

		File mostRecent = null;
		for (File log : crashLogs) {
			if (mostRecent == null || log.getName().compareTo(mostRecent.getName()) > 0) {
				mostRecent = log;
			}
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
