package com.codemarvels.boshservlet.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class AppLogger {

	private AppLogger()
    {//hidden constructor
    }

    static Logger logger;
    static
    {
            logger = Logger.getLogger(AppLogger.class);
    }
    public static void debug(Object message, Throwable t) {
            logger.debug(message, t);
    }
    public static void debug(Object message) {
            logger.debug(message);
    }
    public static void error(Object message, Throwable t) {
            t.printStackTrace();
            logger.error(message, t);
    }
    public static final Level getLevel() {
            return logger.getLevel();
    }
    public static void info(Object message, Throwable t) {
            logger.info(message, t);
    }
    public static void info(Object message) {
            logger.info(message);
    }
    public static void log(Priority priority, Object message, Throwable t) {
            logger.log(priority, message, t);
    }
    public static void log(Priority priority, Object message) {
            logger.log(priority, message);
    }
    public static void log(String callerFQCN, Priority level, Object message,
                    Throwable t) {
            logger.log(callerFQCN, level, message, t);
    }
    public static void warn(Object message, Throwable t) {
            logger.warn(message, t);
    }
    public static void warn(Object message) {
            logger.warn(message);
    }

}
