package fr.craftyourmind.launcher.logger;

public enum LogType {
    Debug("console_paste"),
    Intermediaire("console_pasted"),
    Minimal("console_pasted");

    private String text;

    LogType(String text) {
        this.text = text;
    }

    public boolean includes(LogType other) {
        return (other.compareTo(this) >= 0);
    }

    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public static LogType fromString(String text) {
        if (text != null)
            for (LogType b : values()) {
                if (text.equalsIgnoreCase(b.text))
                    return b;
            }
        return null;
    }
}
