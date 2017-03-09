package net.gtn.dimensionalpocket.common.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;


public class DPLogger {

	public static DPLogger INSTANCE = new DPLogger();

	private Logger logger;

	private DPLogger() {
	}

	public static void init(Logger logger) {
		INSTANCE.logger = logger;
	}

	public static void log(Level logLevel, String levelName, Object object) {
		INSTANCE.logger.log(logLevel, levelName + " " + object);
	}

	public static void log(Level logLevel, String levelName, Class<?> srcClass, Object object, Throwable t) {
		StringBuilder sb = new StringBuilder(levelName);
		sb.append(" ");
		if (srcClass != null) {
			sb.append("<").append(srcClass.getSimpleName()).append("> ");
		}
		sb.append(object);
		INSTANCE.logger.log(logLevel, sb.toString(), t);
	}

	public static void info(Object object) {
		log(Level.INFO, "[INFO]", null, object, null);
	}

	public static void info(Object object, Class<?> srcClass) {
		log(Level.INFO, "[INFO]", srcClass, object, null);
	}

	public static void debug(Object object) {
		debug(object, null);
	}

	public static void debug(Object object, Class<?> srcClass) {
		log(Level.DEBUG, "[DEBUG]", srcClass, object, null);
	}

	public static void warning(Object object) {
		warning(object, null);
	}

	public static void warning(Object object, Class<?> srcClass) {
		log(Level.WARN, "[WARNING]", srcClass, object, null);
	}

	public static void severe(Object object) {
		severe(object, null);
	}

	public static void severe(Object object, Throwable t) {
		log(Level.FATAL, "[SEVERE]", null, object, t);
	}

	public static Logger getLogger() {
		return INSTANCE.logger;
	}

}
