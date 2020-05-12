package utilities;

import javafx.scene.control.TextField;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean checkForAllEmptyInputs(TextField... inputs) {
        for (TextField inputField: inputs){
            if (inputField.getText().isEmpty()) return false;
        }
        return true;
    }
    public static boolean checkForAnyEmptyInputs(TextField... inputs) {
        for (TextField inputField: inputs){
            if (!inputField.getText().isEmpty()) return true;
        }
        return false;
    }


    public static boolean timeInputProperLength(TextField ... timeArgs){
        for (TextField tme: timeArgs) {
            if ((tme.getText().length() > 2 || tme.getText().isEmpty())) return false;
        }
        return true;
    }

    public static boolean timeInputNumbersOnly(TextField ... timeArgs){
        String regexDigits = "[0-9]+";
        for (TextField tme: timeArgs) {
            if (!Pattern.matches(regexDigits, tme.getText()) || tme.getText().isEmpty()) return false;
        }
        return true;
    }
}
