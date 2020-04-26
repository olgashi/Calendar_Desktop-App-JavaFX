package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertMessage {
    public static void display(String messageToDisplay){
        Alert alertEmptyFields = new Alert(Alert.AlertType.WARNING, messageToDisplay);
        alertEmptyFields.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                return;
            }
        });
    }
}
