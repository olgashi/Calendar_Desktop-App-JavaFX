package view_controller;
//TODO: style with css when finished
//TODO: deploy in a web app
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable {

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
        //TODO: extract method that queries the database and place it in dbQuery under utilities
        boolean queryResult = false;
        PreparedStatement pstmt = DBConnection.getConnection().prepareStatement("SELECT user.userId FROM U071A3.user WHERE userName=? AND password=?");
        pstmt.setString(1, userN);
        pstmt.setString(2, pass);

        try {
            ResultSet rs = pstmt.executeQuery();
            queryResult = rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (pstmt != null) pstmt.close();
        }
        return queryResult;
    }
// TODO: again, this method is in many other places, extract/refactor to use just one for all the cases
    public void changeSceneMainWindowView(ActionEvent event) throws IOException {
        Parent mainWindowViewParent = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene addPartViewScene = new Scene(mainWindowViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addPartViewScene);
        window.centerOnScreen();
        window.show();
    }

    public void loginButtonClickEvent(ActionEvent event) {
        String userNameEntered = usernameTextField.getText();
        String passwordEntered = passwordTextField.getText();

        if (!userNameEntered.isEmpty() || !passwordEntered.isEmpty()) {
            try {
                //if getUserData returns true proceed to next window
                if (determineIfUserExists(userNameEntered, passwordEntered)) {
                    System.out.println("user found");
                    loginInvalidWarningText.setText("");
                    changeSceneMainWindowView(event);
                }
                else {
                    System.out.println("user not found");
                    loginInvalidWarningText.setText("Username and Password combination is invalid.");
                }
            } catch (NullPointerException | SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            loginInvalidWarningText.setText("Username and Password cannot be empty.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
