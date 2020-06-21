package fr.craftyourmind.launcher.control;

import fr.craftyourmind.launcher.Main;
import fr.craftyourmind.launcher.MainAux;
import fr.craftyourmind.launcher.logger.LogLevel;
import fr.craftyourmind.launcher.logger.LogType;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleController implements Initializable {
    public void initialize(URL location, ResourceBundle resources) {}

    public void level(ActionEvent actionEvent) {
        MainAux.console.logType = ((ComboBox<LogType>)actionEvent.getSource()).getValue();
        switch (MainAux.console.logType.ordinal()) {
            case 1:
            case 2:
                MainAux.console.logLevel = LogLevel.INFO;
                break;
            case 3:
                MainAux.console.logLevel = LogLevel.DEBUG;
                break;
        }
        MainAux.console.refreshLogs();
    }
}
