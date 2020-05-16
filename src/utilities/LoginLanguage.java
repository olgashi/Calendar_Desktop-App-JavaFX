package utilities;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class LoginLanguage {
    public static void setLoginWindowLabels(String userRegion, Text userNameLabel, Text userPasswordLabel, Text mainLabel, Button loginButton) {
        switch (userRegion) {
            case "FR":
                mainLabel.setText("Veuillez fournir un nom d'utilisateur et un mot de passe pour vous connecter.");
                userNameLabel.setText("Nom d'utilisateur");
                userPasswordLabel.setText("Mot de passe");
                loginButton.setText("S'identifier");
                break;
            case "US":
                mainLabel.setText("Please provide username and password to log in.");
                userNameLabel.setText("Username");
                userPasswordLabel.setText("Password");
                loginButton.setText("Log in");
                break;
            default:    //set default to spanish
                mainLabel.setText("Proporcione nombre de usuario y contraseña para iniciar sesión.");
                userNameLabel.setText("Nombre de usuario");
                userPasswordLabel.setText("Contraseña");
                loginButton.setText("Iniciar sesión");
                break;
        }

    }

    public static void userNamePassInvalidComboMessage(String userRegion, Text errorMessageText) {
        switch (userRegion) {
            case "FR":
                errorMessageText.setText("La combinaison nom d'utilisateur / mot de passe n'est pas valide.");
                break;
            case "US":
                errorMessageText.setText("Username and Password combination is invalid.");
                break;
            default:    //set default to spanish
                errorMessageText.setText("La combinación de nombre de usuario y contraseña no es válida.");
                break;
        }
    }
    public static void userNamePassEmptyMessage(String userRegion, Text errorMessageText) {
        switch (userRegion) {
            case "FR":
                errorMessageText.setText("Le nom d'utilisateur et le mot de passe ne peuvent pas être vides.");
                break;
            case "US":
                errorMessageText.setText("Username and Password cannot be empty.");
                break;
            default:    //set default to spanish
                errorMessageText.setText("El nombre de usuario y la contraseña no pueden estar vacíos.");
                break;
        }
    }
}
