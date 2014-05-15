package net.gtn.dimensionalpocket.common.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;

public class DPLogger {

	public static DPLogger log = new DPLogger();
	static Side side;

	private Logger dpLogger;

	public DPLogger() {
	}

	public static void init() {
		log.dpLogger = LogManager.getLogger("DP");
	}

	public static void log(Level logLevel, Object object) {
		if (object == null)
			object = "null";
		log.dpLogger.log(logLevel, object.toString());
	}

	public static void info(Object object) {
		log(Level.INFO, "[INFO] " + object);
	}

	public static void debug(Object object) {
		log(Level.DEBUG, "[DEBUG] " + object);
	}

	public static void warning(Object object) {
		log(Level.WARN, "[WARNING] " + object);
	}

	public static void severe(Object object) {
		log(Level.FATAL, "[SEVERE] " + object);
	}

	public static Logger getLogger() {
		return log.dpLogger;
	}

}
