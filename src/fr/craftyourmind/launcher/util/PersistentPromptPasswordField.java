package fr.craftyourmind.launcher.util;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PersistentPromptPasswordField extends PasswordField {

    public PersistentPromptPasswordField() {
        this("");
    }

    PersistentPromptPasswordField(String prompt) {
        super();
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