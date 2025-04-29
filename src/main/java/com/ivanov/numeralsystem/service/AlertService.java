package com.ivanov.numeralsystem.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

public class AlertService {
    private static final String ERROR_STYLE =
            ".alert {" +
                    "    -fx-background-color: #23233a;" +
                    "    -fx-border-color: linear-gradient(to right, #ff4081, #7e57c2);" +
                    "    -fx-border-width: 2px;" +
                    "    -fx-border-radius: 16px;" +
                    "    -fx-background-radius: 16px;" +
                    "    -fx-effect: dropshadow(gaussian, rgba(40,32,80,0.4), 16, 0, 4, 4);" +
                    "}" +
                    ".alert .header-panel {" +
                    "    -fx-background-color: linear-gradient(to right, #7e57c2, #4527a0);" +
                    "    -fx-padding: 10px;" +
                    "}" +
                    ".alert .content.label {" +
                    "    -fx-font-size: 14px;" +
                    "    -fx-text-fill: #e1bee7;" +
                    "    -fx-font-weight: 600;" +
                    "}" +
                    ".alert .button {" +
                    "    -fx-background-color: linear-gradient(to bottom, #7e57c2, #673ab7);" +
                    "    -fx-text-fill: white;" +
                    "    -fx-font-weight: bold;" +
                    "    -fx-background-radius: 8;" +
                    "    -fx-padding: 8 16;" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(126,87,194,0.3), 8, 0, 0, 2);" +
                    "}" +
                    ".alert .button:hover {" +
                    "    -fx-background-color: linear-gradient(to bottom, #8e67d2, #774ac7);" +
                    "}";

    private static final String SUCCESS_STYLE =
            ".alert {" +
                    "    -fx-background-color: #23233a;" +
                    "    -fx-border-color: linear-gradient(to right, #4caf50, #2e7d32);" +
                    "    -fx-border-width: 2px;" +
                    "    -fx-border-radius: 16px;" +
                    "    -fx-background-radius: 16px;" +
                    "}" +
                    ".alert .header-panel {" +
                    "    -fx-background-color: linear-gradient(to right, #4caf50, #2e7d32);" +
                    "}" +
                    ".alert .button {" +
                    "    -fx-background-color: linear-gradient(to bottom, #4caf50, #2e7d32);" +
                    "}";

    public static void showError(String title, String message) {
        Alert alert = createStyledAlert(title, message, ERROR_STYLE);
        alert.showAndWait();
    }

    public static void showSuccess(String title, String message) {
        Alert alert = createStyledAlert(title, message, SUCCESS_STYLE);
        alert.showAndWait();
    }

    private static Alert createStyledAlert(String title, String message, String style) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle(title);
        alert.setHeaderText(null);

        Label content = new Label(message);
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 14px; -fx-text-fill: #e1bee7;");
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.getDialogPane().setStyle(style);
        alert.getDialogPane().getStylesheets().add(
                AlertService.class.getResource("/com/ivanov/numeralsystem/service/alert.css").toExternalForm()
        );

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        return alert;
    }
}
