package utilities;

import javafx.scene.control.TextField;

public class InputValidation {
    public static boolean checkForAllEmptyInputs(TextField... inputs) {
        for (TextField inputField: inputs){
            if (!inputField.getText().isEmpty()) return false;
        }
        return true;
    }
    public static boolean checkForAnyEmptyInputs(TextField... inputs) {
        for (TextField inputField: inputs){
            if (inputField.getText().isEmpty()) return true;
        }
        return false;
    }
}
