package nl.utwente.ewi.qwirkle.util;

public class Logger {
    public static final int OFF = 0;
    public static final int FATAL = 1;
    public static final int ERROR = 2;
    public static final int WARN = 3;
    public static final int INFO = 4;
    public static final int DEBUG = 5;
    public static final int ALL = 6;

    private static int level;

    public static void setLevel(int level) {
        Logger.level = level;
    }

    public static void fatal(Object message) {
        if (Logger.FATAL <= level) {
            System.err.println(message);
        }
    }

    public static void error(Object message) {
        if (Logger.ERROR <= level) {
            System.err.println(message);
        }
    }

    public static void warn(Object message) {
        if (Logger.WARN <= level) {
            System.out.println(message);
        }
    }

    public static void info(Object message) {
        if (Logger.INFO <= level) {
            System.out.println(message);
        }
    }

    public static void debug(Object message) {
        if (Logger.DEBUG <= level) {
            System.out.println(message);
        }
    }
}
