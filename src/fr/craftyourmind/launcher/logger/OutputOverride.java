package fr.craftyourmind.launcher.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class OutputOverride extends PrintStream {
    final LogLevel level;

    public OutputOverride(OutputStream str, LogLevel type) {
        super(str);
        this.level = type;
    }

    public void write(byte[] b) throws IOException {
        String text = (new String(b)).trim();
        if (!text.equals("") && !text.equals("\n"))
            Logger.log(text, this.level, null);
    }

    public void write(byte[] buf, int off, int len) {
        String text = (new String(buf, off, len)).trim();
        if (!text.equals("") && !text.equals("\n"))
            Logger.log(text, this.level, null);
    }

    public void write(int b) {
        String text = String.valueOf(b);
        if (!text.equals("") && !text.equals("\n"))
            Logger.log(text, this.level, null);
    }
}
