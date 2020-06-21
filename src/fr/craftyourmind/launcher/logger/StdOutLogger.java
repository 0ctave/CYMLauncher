package fr.craftyourmind.launcher.logger;

import java.io.PrintStream;

public class StdOutLogger implements ILogListener {
    private static final PrintStream realStderr = System.err;

    private static final PrintStream realStdout = System.out;

    private LogType logType = LogType.Intermediaire;

    public void setLogSource(LogSource logSource) {
        this.logSource = logSource;
    }

    private LogSource logSource = LogSource.LAUNCHER;

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    private LogLevel logLevel = LogLevel.UNKNOWN;

    public StdOutLogger(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public StdOutLogger(LogSource logSource) {
        this.logSource = logSource;
    }

    public StdOutLogger(LogLevel logLevel, LogSource logSource) {
        this.logLevel = logLevel;
        this.logSource = logSource;
    }

    public void onLogEvent(LogEntry entry) {
        if (this.logSource != LogSource.ALL && entry.source != this.logSource)
            return;
        if (!this.logLevel.includes(entry.level))
            return;
        if (entry.level == LogLevel.ERROR) {
            realStderr.println(entry.toString(this.logType));
        } else {
            realStdout.println(entry.toString(this.logType));
        }
    }

    public StdOutLogger() {}
}