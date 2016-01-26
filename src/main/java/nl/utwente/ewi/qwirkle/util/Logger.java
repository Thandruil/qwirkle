package nl.utwente.ewi.qwirkle.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * This class handles all the logging in the system. It has different levels of logging and PrintStreams can be added for the output.
 */
public class Logger {
    /**
     * Debug level: No debug at all.
     */
    public static final int OFF = 0;

    /**
     * Debug level: Only fatal errors.
     */
    public static final int FATAL = 1;

    /**
     * Debug level: Only fatal and normal errors.
     */
    public static final int ERROR = 2;

    /**
     * Debug level: Fatal errors, normal errors and warnings.
     */
    public static final int WARN = 3;

    /**
     * Debug level: Fatal errors, normal errors, warnings and informational notes.
     */
    public static final int INFO = 4;

    /**
     * Debug level: Fatal errors, normal errors, warnings, informational notes and debug notes.
     */
    public static final int DEBUG = 5;

    /**
     * Debug level: Show all messages.
     */
    public static final int ALL = 6;

    /**
     * Maps PrintStreams to debug levels.
     */
    private static final Map<PrintStream, Integer> outs = new HashMap<>();

    /**
     * Called when a fatal error occurred.
     * @param message The error message.
     */
    public static void fatal(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.FATAL <= outs.get(out)) {
                out.printf("[FATAL] %s\n", message);
            }
        }
    }

    /**
     * Called when a fatal error occurred.
     * @param message The error message.
     * @param e The original Throwable.
     */
    public static void fatal(Object message, Throwable e) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.FATAL <= outs.get(out)) {
                out.printf("[FATAL] %s\n", message);
                out.printf("[FATAL] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    /**
     * Called when a normal error occurred.
     * @param message The error message.
     */
    public static void error(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.ERROR <= outs.get(out)) {
                out.printf("[ERROR] %s\n", message);
            }
        }
    }

    /**
     * Called when a normal error occurred.
     * @param message The error message.
     * @param e The original Throwable.
     */
    public static void error(Object message, Throwable e){
        for (PrintStream out : outs.keySet()) {
            if (Logger.ERROR <= outs.get(out)) {
                out.printf("[ERROR] %s\n", message);
                out.printf("[ERROR] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    /**
     * Called when a warning is given.
     * @param message The warning message.
     */
    public static void warn(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.WARN <= outs.get(out)) {
                out.printf("[WARN] %s\n", message);
            }
        }
    }

    /**
     * Called when a warning is given.
     * @param message The warning message.
     * @param e The original Throwable.
     */
    public static void warn(Object message, Throwable e){
        for (PrintStream out : outs.keySet()) {
            if (Logger.WARN <= outs.get(out)) {
                out.printf("[WARN] %s\n", message);
                out.printf("[WARN] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    /**
     * Called then an info message is given.
     * @param message The info message.
     */
    public static void info(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.INFO <= outs.get(out)) {
                out.printf("[INFO] %s\n", message);
            }
        }
    }

    /**
     * Called then a debug message is given.
     * @param message The debug message.
     */
    public static void debug(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.DEBUG <= outs.get(out)) {
                out.printf("[DEBUG] %s\n", message);
            }
        }
    }

    /**
     * Adds an OutputStream to the Map of OutputStreams. The messages in this class are automatically sent to the OutputStreams based on their given debug level.
     * @param level The debug level the OutputStream should listen to.
     * @param out The OutputStream which should be added.
     */
    public static void addOutputStream(int level, OutputStream out) {
        if (level >= 0 && out != null) {
            outs.put(new PrintStream(out), level);
        }
    }
}
