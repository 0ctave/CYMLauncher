package fr.craftyourmind.launcher.logger;

public enum LogLevel {
    DEBUG, INFO, WARN, ERROR, UNKNOWN;

    public boolean includes(LogLevel other) {
        return (other.compareTo(this) >= 0);
    }
}
