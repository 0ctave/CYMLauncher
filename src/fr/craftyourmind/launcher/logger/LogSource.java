package fr.craftyourmind.launcher.logger;

public enum LogSource {
    ALL("All"),
    LAUNCHER("Launcher"),
    EXTERNAL("Minecraft");

    private String humanReadableName;

    LogSource(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }

    public String toString() {
        return (this.humanReadableName == null) ? (name().substring(0, 1) + name().substring(1).toLowerCase()) : this.humanReadableName;
    }
}
