package fr.craftyourmind.launcher.control;

import fr.craftyourmind.launcher.Main;
import fr.craftyourmind.launcher.MainAux;
import fr.craftyourmind.launcher.util.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BetaController implements Initializable {

    @FXML
    private Button yes;

    @FXML
    private Button no;

    private MainAux main;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        main = MainAux.getInstance();
    }

    @FXML
    public void beta(ActionEvent actionEvent) {
        if(actionEvent.getSource() == yes)
            Launcher.update(true, main.username, main.token, main.uuid);
        else if (actionEvent.getSource() == no)
            Launcher.update(false, main.username, main.token, main.uuid);
        ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
