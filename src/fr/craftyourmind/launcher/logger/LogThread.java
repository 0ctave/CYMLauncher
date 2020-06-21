package fr.craftyourmind.launcher.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogThread extends Thread {
    private BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();

    private List<ILogListener> listeners;

    public LogThread(List<ILogListener> listeners) {
        this.listeners = listeners;
        setDaemon(true);
    }

    public void run() {
        setName("Log dispatcher");
        try {
            LogEntry entry;
            while ((entry = this.logQueue.take()) != null) {
                if (!this.listeners.isEmpty()) {
                    List<ILogListener> tempListeners = new ArrayList<>();
                    tempListeners.addAll(this.listeners);
                    for (ILogListener listener : tempListeners)
                        listener.onLogEvent(entry);
                }
            }
        } catch (InterruptedException interruptedException) {}
    }

    public void handleLog(LogEntry logEntry) {
        try {
            this.logQueue.put(logEntry);
        } catch (InterruptedException interruptedException) {}
    }
}
