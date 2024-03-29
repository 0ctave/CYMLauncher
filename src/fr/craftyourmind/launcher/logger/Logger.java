package fr.craftyourmind.launcher.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Logger {
    private static final List<ILogListener> listeners = new ArrayList<>();

    private static final Vector<LogEntry> logEntries = new Vector<>();

    private static LogThread logThread = new LogThread(listeners);

    static {
        logThread.start();
    }

    public static void log(LogEntry entry) {
        logEntries.add(entry);
        logThread.handleLog(entry);
    }

    public static void log(String message, LogLevel level, Throwable t) {
        log((new LogEntry()).level(level).message(message).cause(t));
    }

    public static void logDebug(String message) {
        logDebug(message, null);
    }

    public static void logInfo(String message) {
        logInfo(message, null);
    }

    public static void logWarn(String message) {
        logWarn(message, null);
    }

    public static void logError(String message) {
        logError(message, null);
    }

    public static void logDebug(String message, Throwable t) {
        log(message, LogLevel.DEBUG, t);
    }

    public static void logInfo(String message, Throwable t) {
        log(message, LogLevel.INFO, t);
    }

    public static void logWarn(String message, Throwable t) {
        log(message, LogLevel.WARN, t);
    }

    public static void logError(String message, Throwable t) {
        log(message, LogLevel.ERROR, t);
    }

    public static void addListener(ILogListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(ILogListener listener) {
        listeners.remove(listener);
    }

    public static List<LogEntry> getLogEntries() {
        return new Vector<>(logEntries);
    }

    public static String getLogs() {
        return getLogs(LogType.Intermediaire);
    }

    private static String getLogs(LogType type) {
        StringBuilder logStringBuilder = new StringBuilder();
        for (LogEntry entry : getLogEntries())
            logStringBuilder.append(entry.toString(type)).append("\n");
        return logStringBuilder.toString();
    }
}