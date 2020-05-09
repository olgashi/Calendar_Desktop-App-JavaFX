package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class LoginLanguage {


    public static void setLoginWindowLabels(String userRegion, Text userNameLabel, Text userPasswordLabel, Text mainLabel, Button loginButton ){
        switch (userRegion) {
            case "FR":
                mainLabel.setText("Veuillez fournir un nom d'utilisateur et un mot de passe pour vous connecter.");
                userNameLabel.setText("Nom d'utilisateur");
                userPasswordLabel.setText("Mot de passe");
                loginButton.setText("S'identifier");
                break;
            case "EN":
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
}
