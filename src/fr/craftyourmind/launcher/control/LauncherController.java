package fr.craftyourmind.launcher.control;

import fr.craftyourmind.launcher.MainAux;
import fr.craftyourmind.launcher.util.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button facebook;

    @FXML
    private Button twitter;

    @FXML
    private Button youtube;

    @FXML
    private Button settings;

    @FXML
    private Label invalidCredential;

    private MainAux main;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        main = MainAux.getInstance();
    }

    @FXML
    private void play(ActionEvent actionEvent) throws Exception {
        if (this.username.getText().isEmpty() || this.password.getText().isEmpty()) {
            main.cancel();
        } else {
            ((Button)actionEvent.getSource()).setDisable(true);
            this.settings.setDisable(true);
            this.username.setDisable(true);
            this.password.setDisable(true);
            this.invalidCredential.setVisible(false);
            new Auth(this.username.getText(), this.password.getText());
        }
    }

    @FXML
    private void social(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.facebook)
            main.showDoc("https://www.facebook.com/pages/Craft-Your-Mind/1642931989260122?fref=ts");
        if (actionEvent.getSource() == this.twitter)
            main.showDoc("https://twitter.com/Craft_Your_Mind");
        if (actionEvent.getSource() == this.youtube)
            main.showDoc("https://www.youtube.com/channel/UCAAGkWQA5rc2A36HEj839Yw");
    }

    @FXML
    public void settings() {
        main.showSettings();
    }
}