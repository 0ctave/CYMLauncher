package fr.craftyourmind.launcher.control;

import fr.craftyourmind.launcher.Main;
import fr.craftyourmind.launcher.MainAux;
import fr.craftyourmind.launcher.util.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private Button site;

    @FXML
    private Button teamspeak;

    @FXML
    private Button password;

    @FXML
    private Button username;

    @FXML
    private Button skin;

    @FXML
    private Button resourcepack;

    @FXML
    private Button shaders;

    @FXML
    private Button screen;

    @FXML
    private ImageView usernameValid;

    @FXML
    private ImageView passwordValid;

    @FXML
    private Slider ram;

    public boolean usernameSave = true;

    public boolean passwordSave = true;

    private String usernameText = "";

    private MainAux main;

    private boolean console = false;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        main = MainAux.getInstance();
    }

    @FXML
    public void settingsDocument(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.site)
            main.showDoc("https://craftyourmind.fr");
        if (actionEvent.getSource() == this.teamspeak)
            main.showDoc("ts3server://ts.craftyourmind.fr?nickname=" + this.usernameText);
        if (actionEvent.getSource() == this.username)
            this.usernameValid.setVisible(this.usernameSave = !this.usernameSave);
        if (actionEvent.getSource() == this.password)
            this.passwordValid.setVisible(this.passwordSave = !this.passwordSave);
        if (actionEvent.getSource() == this.skin)
            main.getHostServices().showDocument(Launcher.LC_DIR.getAbsolutePath() + "\\resourcepacks");
        if( actionEvent.getSource() == this.resourcepack)
            main.getHostServices().showDocument(Launcher.LC_DIR.getAbsolutePath() + "\\resourcepacks");
        if (actionEvent.getSource() == this.shaders)
            main.getHostServices().showDocument(Launcher.LC_DIR.getAbsolutePath() + "\\shaderpacks");
        if (actionEvent.getSource() == this.screen)
            main.getHostServices().showDocument(Launcher.LC_DIR.getAbsolutePath() + "\\screenshots");
    }

    @FXML
    public void launcher() throws IOException {
        (main.getConfig()).config.put("usernameSave", this.usernameSave);
        (main.getConfig()).config.put("passwordSave", this.passwordSave);
        (main.getConfig()).config.put("ram", this.ram.getValue());
        main.getConfig().save();
        main.showLauncher();
    }

    @FXML
    public void console() {
        if (!this.console) {
            main.getConsole().show();
            this.console = true;
        } else {
            main.getConsole().hide();
            this.console = false;
        }
    }

}