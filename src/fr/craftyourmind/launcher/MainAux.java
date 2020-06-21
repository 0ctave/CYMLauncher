package fr.craftyourmind.launcher;

import fr.craftyourmind.launcher.config.Config;
import fr.craftyourmind.launcher.logger.*;
import fr.craftyourmind.launcher.util.Console;
import fr.craftyourmind.launcher.util.EncryptionUtil;
import fr.craftyourmind.launcher.util.HyperLinkRedirectListener;
import fr.craftyourmind.launcher.util.Launcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainAux extends Application {

    public static Console console;
    private static MainAux instance;
    private static double xOffset = 0.0D;
    private static double yOffset = 0.0D;
    public String uuid;
    public String token;
    public String username;
    public Config config;
    public File settings = new File(Launcher.LC_DIR, "config.yaml");
    public File tokens = new File(Launcher.LC_DIR, "tokens.txt");
    public File usernameFile = new File(Launcher.LC_DIR, "username.txt");
    public File passwordFile = new File(Launcher.LC_DIR, "password.txt");
    private Scene mainScene;
    private FXMLLoader mainFXML;
    private FXMLLoader launcherFXML;
    private Stage mainStage;
    private WebEngine webEngine;
    private AnchorPane launcherPane;
    private AnchorPane settingsPane;
    private ProgressBar progressBar;

    private String players;

    public static void load(String[] args) throws IOException {
        Launcher.LC_DIR.mkdir();
        File log = new File(Launcher.infos.getGameDir(), "logs.txt");
        if (!log.exists())
            log.createNewFile();
        Logger.addListener(new StdOutLogger(LogLevel.DEBUG, LogSource.ALL));
        Logger.addListener(new LogWriter(log, LogSource.LAUNCHER));
        System.setOut(new OutputOverride(System.out, LogLevel.INFO));
        System.setErr(new OutputOverride(System.err, LogLevel.ERROR));
        launch(args);
    }

    public static MainAux getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (!this.tokens.exists())
            this.tokens.createNewFile();
        if (!this.settings.exists()) {
            this.settings.createNewFile();
            config = new Config(this.settings.getAbsolutePath());
            config.generate();
        } else {
            config = new Config(this.settings.getAbsolutePath());
        }
        if (!this.usernameFile.exists()) {
            this.usernameFile.createNewFile();
        }
        if (!this.passwordFile.exists()) {
            this.passwordFile.createNewFile();
        }

        instance = this;
        Parent root = (mainFXML = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"))).load();
        this.mainStage = stage;

        mainStage.getIcons().add(new Image("/images/icon.png"));
        mainStage.setTitle("CraftYourMind");
        mainStage.setScene(mainScene = new Scene(root, 1124, 639));
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setOnCloseRequest(e -> Platform.exit());

        ((AnchorPane) mainFXML.getNamespace().get("mainPane")).getChildren().addAll(launcherPane = (launcherFXML = new FXMLLoader(getClass().getResource("/fxml/Launcher.fxml"))).load());
        settingsPane = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml")).load();

        loadWebEngine();
        setupConsole();

        this.progressBar = ((ProgressBar) this.launcherFXML.getNamespace().get("progressBar"));


        /*try {
            URL url = new URL("http://repo.craftyourmind.fr/launcher/util/players.php");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            players = "Joueurs connectés : " + reader.readLine();
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

        if (players == null) {
            players = "Joueurs connectés : Offline";
        }

        ((Label) this.launcherFXML.getNamespace().get("playersLabel")).setText(players);

        config.load();
        if (config.getBoolean("usernameSave") && usernameFile.exists())
            try {
                BufferedReader readerPass = new BufferedReader(new FileReader(this.usernameFile));
                String username = readerPass.readLine();
                if (username != null)
                    ((TextField) this.launcherFXML.getNamespace().get("username")).setText(username);
                readerPass.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (config.getBoolean("passwordSave") && passwordFile.exists()) {
            EncryptionUtil encryptionUtil = new EncryptionUtil();
            try {
                BufferedReader readerPass = new BufferedReader(new FileReader(this.passwordFile));
                String password = readerPass.readLine();
                if (password != null)
                    ((PasswordField) this.launcherFXML.getNamespace().get("password")).setText(encryptionUtil.decrypt("cym%$$", password));
                readerPass.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mainStage.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, javafx.event.Event::consume);

        mainStage.show();
        loadDragnDrop();
    }

    private void loadWebEngine() {
        WebView webView = (WebView) this.launcherFXML.getNamespace().get("webview");
        webView.setContextMenuEnabled(false);
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webView.getEngine().getLoadWorker().stateProperty().addListener(new HyperLinkRedirectListener(webView));
        /*webView.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
            Set<Node> deadSeaScrolls = webView.lookupAll(".scroll-bar");
            for (Node scroll : deadSeaScrolls) {
                scroll.setVisible(false);
            }
        });*/
        Platform.runLater(() -> this.webEngine.load("http://push.craftyourmind.fr/news_launcher.php"));
    }

    public void showDoc(String link) {
        this.getHostServices().showDocument(link);
    }

    private void loadDragnDrop() {
        mainScene.setOnMousePressed(event -> {
            xOffset = this.mainStage.getX() - event.getScreenX();
            yOffset = this.mainStage.getY() - event.getScreenY();
        });
        mainScene.setOnMouseDragged(event -> {
            this.mainStage.setX(event.getScreenX() + xOffset);
            this.mainStage.setY(event.getScreenY() + yOffset);
        });
    }

    public Stage getStage() {
        return this.mainStage;
    }

    private void setupConsole() throws IOException {
        console = new Console();
        Logger.addListener(console);
    }

    public Config getConfig() {
        return config;
    }

    public Stage getConsole() {
        return console.console;
    }

    public void showSettings() {
        ((AnchorPane) mainFXML.getNamespace().get("mainPane")).getChildren().removeAll(launcherPane);
        ((AnchorPane) mainFXML.getNamespace().get("mainPane")).getChildren().addAll(settingsPane);
    }

    public void showLauncher() {
        ((AnchorPane) mainFXML.getNamespace().get("mainPane")).getChildren().removeAll(settingsPane);
        ((AnchorPane) mainFXML.getNamespace().get("mainPane")).getChildren().addAll(launcherPane);
    }

    public void cancel() {
        ((Label) this.launcherFXML.getNamespace().get("invalidCredential")).setVisible(true);
        ((Button) this.launcherFXML.getNamespace().get("play")).setDisable(false);
        ((Button) this.launcherFXML.getNamespace().get("settings")).setDisable(false);
        ((TextField) this.launcherFXML.getNamespace().get("username")).setDisable(false);
        ((PasswordField) this.launcherFXML.getNamespace().get("password")).setDisable(false);
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }
}