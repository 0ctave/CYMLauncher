package fr.craftyourmind.launcher;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main {

    private static boolean java;

    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }));
        MainAux.load(args);
    }

    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }
}
