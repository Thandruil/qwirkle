package nl.utwente.ewi.qwirkle.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class Logger {
    public static final int OFF = 0;
    public static final int FATAL = 1;
    public static final int ERROR = 2;
    public static final int WARN = 3;
    public static final int INFO = 4;
    public static final int DEBUG = 5;
    public static final int ALL = 6;

    private static final Map<PrintStream, Integer> outs = new HashMap<>();

    public static void fatal(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.FATAL <= outs.get(out)) {
                out.printf("[FATAL] %s\n", message);
            }
        }
    }

    public static void fatal(Object message, Throwable e) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.FATAL <= outs.get(out)) {
                out.printf("[FATAL] %s\n", message);
                out.printf("[FATAL] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    public static void error(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.ERROR <= outs.get(out)) {
                out.printf("[ERROR] %s\n", message);
            }
        }
    }

    public static void error(Object message, Throwable e){
        for (PrintStream out : outs.keySet()) {
            if (Logger.ERROR <= outs.get(out)) {
                out.printf("[ERROR] %s\n", message);
                out.printf("[ERROR] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    public static void warn(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.WARN <= outs.get(out)) {
                out.printf("[WARN] %s\n", message);
            }
        }
    }

    public static void warn(Object message, Throwable e){
        for (PrintStream out : outs.keySet()) {
            if (Logger.WARN <= outs.get(out)) {
                out.printf("[WARN] %s\n", message);
                out.printf("[WARN] The exception was %s: %s\n", e.getClass(), e.getMessage());
            }
        }
    }

    public static void info(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.INFO <= outs.get(out)) {
                out.printf("[INFO] %s\n", message);
            }
        }
    }

    public static void debug(Object message) {
        for (PrintStream out : outs.keySet()) {
            if (Logger.DEBUG <= outs.get(out)) {
                out.printf("[DEBUG] %s\n", message);
            }
        }
    }

    public static void addOutputStream(int level, OutputStream out) {
        if (level >= 0 && out != null) {
            outs.put(new PrintStream(out), level);
        }
    }
}
