package fr.craftyourmind.launcher.util;

import fr.craftyourmind.launcher.Main;
import fr.craftyourmind.launcher.MainAux;
import fr.theshark34.openlauncherlib.JavaUtil;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;

public class Launcher {
    public static GameInfos infos = new GameInfos("craftyourmind", new GameVersion("1.5.2", GameType.V_1_15_2), new GameTweak[]{});
    public static GameInfos infosBeta = new GameInfos("craftyourmindbeta", new GameVersion("1.5.2", GameType.V_1_15_2), new GameTweak[]{});

    public static final File LC_DIR = infos.getGameDir();
    public static final File LC_DIR_BETA = infosBeta.getGameDir();
    private static boolean launched;

    public static void update(Boolean beta, String pseudo, String token, String UUID) {
        final SUpdate su = new SUpdate("https://cdn.craftyourmind.fr/client/" + (beta ? "beta" : "release") + "/", beta ? LC_DIR_BETA : LC_DIR);
        Thread t = new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    double val;
                    if (BarAPI.getNumberOfTotalBytesToDownload() != 0) {
                        val = (((BarAPI.getNumberOfTotalDownloadedBytes() * 1000D) / BarAPI.getNumberOfTotalBytesToDownload()) / 1000D);
                    } else
                        val = 1.0D;
                    (MainAux.getInstance()).getProgressBar().setProgress(val);
                }
            }
        };
        Thread p = new Thread(() -> {
            try {
                su.start();
                t.interrupt();
                launchGame(pseudo, token, UUID, beta);
            } catch (IOException | LaunchException | BadServerResponseException | ServerDisabledException | BadServerVersionException | ServerMissingSomethingException e) {
                e.printStackTrace();
            }
        });
        t.start();
        p.start();

    }

    public static void launchGame(String username, String MD5, String UUID, boolean beta) throws LaunchException {
        AuthInfos authInfos = new AuthInfos(username, MD5, UUID);
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(beta ? infosBeta : infos, GameFolder.BASIC, authInfos);
        JavaUtil.setJavaCommand(LC_DIR.getAbsolutePath() + "\\runtime\\jre-x64\\bin\\java");
        ExternalLauncher launcher = new ExternalLauncher(profile);
        launcher.launch();
        Platform.runLater(() -> MainAux.getInstance().getStage().hide());
        if(!MainAux.getInstance().getConsole().isShowing())
            System.exit(0);

    }
}
