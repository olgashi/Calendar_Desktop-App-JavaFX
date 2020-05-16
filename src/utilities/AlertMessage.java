package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
// TODO refactor
public class AlertMessage {
    public static boolean display(String messageToDisplay, String alertType) {
        Alert.AlertType alertTypeMethodCall = getAlertType(alertType);
        Alert alertMessage = new Alert(alertTypeMethodCall, messageToDisplay);
        Optional<ButtonType> result = alertMessage.showAndWait();
        return !(result.isPresent() && result.get() == ButtonType.CANCEL);
    }

    private static Alert.AlertType getAlertType(String alertType) {
        alertType = alertType.toLowerCase();
        Alert.AlertType alertTypeMethodCall;
        switch (alertType) {
            case "warning":
                alertTypeMethodCall = Alert.AlertType.WARNING;
                break;
            case "confirmation":
                alertTypeMethodCall = Alert.AlertType.CONFIRMATION;
                break;
            case "information":
                alertTypeMethodCall = Alert.AlertType.INFORMATION;
                break;
            default:
                alertTypeMethodCall = Alert.AlertType.NONE;
                break;
        }
        return alertTypeMethodCall;
    }
}
