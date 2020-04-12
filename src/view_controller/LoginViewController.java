package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utilities.DBConnection;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Button clearLoginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Text loginInvalidWarningText;

    public boolean determineIfUserExists(String userN, String pass) throws SQLException {
        boolean queryResult = false;
        PreparedStatement pstmt = DBConnection.getConnection().prepareStatement("SELECT user.userId FROM U071A3.user WHERE userName=? AND password=?");
        pstmt.setString(1, userN);
        pstmt.setString(2, pass);

        try {
            ResultSet rs = pstmt.executeQuery();
            queryResult = rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            if (pstmt != null) pstmt.close();
        }
        return queryResult;
    }


    public void loginButtonClickEvent() {
        String userNameEntered = usernameTextField.getText();
        String passwordEntered = passwordTextField.getText();

        if (!userNameEntered.isEmpty() || !passwordEntered.isEmpty()) {
            try {
                //if getUserData returns true proceed to next window
                if (determineIfUserExists(userNameEntered, passwordEntered)) {
                    System.out.println("user found");
                    loginInvalidWarningText.setText("");
                }
                else {
                    System.out.println("user not found");
                    loginInvalidWarningText.setText("Username and Password combination is invalid.");
                }
            } catch (NullPointerException | SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            loginInvalidWarningText.setText("Username and Password cannot be empty.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
