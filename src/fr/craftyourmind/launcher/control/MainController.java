package fr.craftyourmind.launcher.control;

import fr.craftyourmind.launcher.Main;
import fr.craftyourmind.launcher.MainAux;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    public void window(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.minimize)
            (MainAux.getInstance()).getStage().setIconified(true);
        if (actionEvent.getSource() == this.close) {
            ((Stage)this.close.getScene().getWindow()).close();
            System.exit(0);
        }
    }
}
