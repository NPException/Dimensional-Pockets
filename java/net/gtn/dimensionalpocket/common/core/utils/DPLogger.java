package net.gtn.dimensionalpocket.common.core.utils;

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

    private static void log(Level logLevel, String levelName, Class<?> srcClass, Object object) {
        StringBuilder sb = new StringBuilder(levelName);
        if (srcClass != null)
            sb.append("<").append(srcClass.getSimpleName()).append("> ");
        sb.append(object);
        log.dpLogger.log(logLevel, sb.toString());
    }

    public static void info(Object object) {
        info(object, null);
    }

    public static void info(Object object, Class<?> srcClass) {
        log(Level.INFO, "[INFO] ", srcClass, object);
    }

    public static void debug(Object object) {
        debug(object, null);
    }

    public static void debug(Object object, Class<?> srcClass) {
        log(Level.DEBUG, "[DEBUG] ", srcClass, object);
    }

    public static void warning(Object object) {
        warning(object, null);
    }

    public static void warning(Object object, Class<?> srcClass) {
        log(Level.WARN, "[WARNING] ", srcClass, object);
    }

    public static void severe(Object object) {
        severe(object, null);
    }

    public static void severe(Object object, Class<?> srcClass) {
        log(Level.FATAL, "[SEVERE] ", srcClass, object);
    }

    public static Logger getLogger() {
        return log.dpLogger;
    }

}
