package fr.craftyourmind.launcher.logger;

import com.beust.jcommander.internal.Maps;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LogEntry {
    private String message = "";

    public LogLevel level = LogLevel.UNKNOWN;

    public LogSource source = LogSource.LAUNCHER;

    private Throwable cause;

    private String location;

    private final String dateString;

    private final Map<LogType, String> messageCache = Maps.newHashMap();

    private static final String dateFormatString = "HH:mm:ss";

    public LogEntry() {
        Date date = new Date();
        this.dateString = (new SimpleDateFormat("HH:mm:ss")).format(date);
        this.location = getLocation(this.cause);
    }

    public LogEntry message(String message) {
        this.message = message;
        if (this.level == LogLevel.UNKNOWN) {
            message = message.toLowerCase();
            if (message.contains("[severe]") || message.contains("[stderr]") || message.contains("[error]")) {
                this.level = LogLevel.ERROR;
            } else if (message.contains("[info]")) {
                this.level = LogLevel.INFO;
            } else if (message.contains("[warning]") || message.contains("[warn]")) {
                this.level = LogLevel.WARN;
            } else if (message.contains("error") || message.contains("severe")) {
                this.level = LogLevel.ERROR;
            } else if (message.contains("warn")) {
                this.level = LogLevel.WARN;
            } else {
                this.level = LogLevel.INFO;
            }
        }
        return this;
    }

    public LogEntry level(LogLevel level) {
        this.level = level;
        return this;
    }

    public LogEntry source(LogSource source) {
        this.source = source;
        return this;
    }

    public LogEntry cause(Throwable cause) {
        if (cause != this.cause)
            this.location = getLocation(cause);
        this.cause = cause;
        return this;
    }

    public LogEntry copyInformation(LogEntry entry) {
        this.message = entry.message;
        this.source = entry.source;
        this.level = entry.level;
        return this;
    }

    public String toString() {
        return toString(LogType.Minimal);
    }

    public String toString(LogType type) {
        if (this.messageCache.containsKey(type))
            return this.messageCache.get(type);
        StringBuilder entryMessage = new StringBuilder();
        if (this.source != LogSource.EXTERNAL) {
            if (type
                    .includes(LogType.Intermediaire))
                entryMessage.append("[").append(this.dateString).append("] ");
            if (type.includes(LogType.Intermediaire))
                entryMessage.append("[").append(this.level).append("] ");
            if (type.includes(LogType.Debug))
                entryMessage.append("in ").append(this.source).append(" ");
            if (this.location != null && type.includes(LogType.Intermediaire))
                entryMessage.append(this.location).append(": ");
        }
        entryMessage.append(this.message);
        if (this.cause != null) {
            entryMessage.append(": ").append(this.cause.toString());
            if (type.includes(LogType.Intermediaire))
                for (StackTraceElement stackTraceElement : this.cause.getStackTrace())
                    entryMessage.append("\n").append(stackTraceElement.toString());
        }
        String message = entryMessage.toString();
        this.messageCache.put(type, message);
        return message;
    }

    private static String getLocation(Throwable t) {
        String location = "";
        if (t != null)
            location = location + getLocation(t.getStackTrace()) + "->";
        location = location + getLocation((new Throwable()).getStackTrace());
        return location;
    }

    private static String getLocation(StackTraceElement[] stackTraceElements) {
        for (StackTraceElement ste : stackTraceElements) {
            if (!ste.getClassName().equals(Logger.class.getName()) && !ste.getClassName().equals(LogEntry.class.getName()))
                return ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1) + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        return "unknown location";
    }

    public static String getDateformatstring() {
        return "HH:mm:ss";
    }
}