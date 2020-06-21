package fr.craftyourmind.launcher.logger;


import java.io.*;

public class LogWriter implements ILogListener {
    private final BufferedWriter logWriter;

    private final LogSource source;

    public LogWriter(File logFile, LogSource source) throws IOException {
        this.source = source;
        this.logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8"));
        this.logWriter.write(logFile + ": written by CYM Launcher" + System.getProperty("line.separator"));
        this.logWriter.flush();
    }

    public void onLogEvent(LogEntry entry) {
        if (entry.source == this.source)
            try {
                this.logWriter.write(entry.toString(LogType.Intermediaire) + System.getProperty("line.separator"));
                this.logWriter.flush();
            } catch (IOException iOException) {}
    }
}
