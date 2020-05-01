package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
// TODO refactor
public class AlertMessage {
    public static boolean display(String messageToDisplay, String alertType) {
        boolean userResponse;
        alertType = alertType.toLowerCase();
        Alert.AlertType alertTypeMethodCall;
        switch (alertType) {
            case "warning":
                alertTypeMethodCall = Alert.AlertType.WARNING;
                break;
            case "confirmation":
                alertTypeMethodCall = Alert.AlertType.CONFIRMATION;
                break;
            default:
                alertTypeMethodCall = Alert.AlertType.WARNING;
                break;
        }
        Alert alertMessage = new Alert(alertTypeMethodCall, messageToDisplay);
        Optional<ButtonType> result = alertMessage.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            userResponse = false;
        } else {
            userResponse = true;
        }
        return userResponse;
    }
}
