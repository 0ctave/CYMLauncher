package fr.craftyourmind.launcher.util;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TextField;

public class PersistentPromptTextField extends TextField {

    public PersistentPromptTextField() {
        this("", "");
    }

    PersistentPromptTextField(String text, String prompt) {
        super(text);
        setPromptText(prompt);
        getStyleClass().add("persistent-prompt");
        refreshPromptVisibility();
        this.getStylesheets().add(getClass().getResource("/css/persistent-prompt.css").toExternalForm());
        textProperty().addListener(observable -> refreshPromptVisibility());
    }

    private void refreshPromptVisibility() {
        final String text = getText();
        if (isEmptyString(text)) {
            getStyleClass().remove("no-prompt");
        } else {
            if (!getStyleClass().contains("no-prompt")) {
                getStyleClass().add("no-prompt");
            }
        }
    }

    private boolean isEmptyString(String text) {
        return text == null || text.isEmpty();
    }
}