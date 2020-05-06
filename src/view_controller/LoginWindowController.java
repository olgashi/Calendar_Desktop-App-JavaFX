package view_controller;
//TODO: style with css when finished
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.DBQuery;
import utilities.HelperQuery;
import utilities.NewWindow;
import java.io.IOException;
import java.net.URL;
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
    @FXML
    private Pane loginWindowPane;

    public boolean determineIfUserExists(String userN, String pass) throws SQLException, IOException {
        DBQuery.createQuery("SELECT user.userId FROM user WHERE userName = " + "'" + userN + "'" + " AND password = " + "'" + pass + "'");
        if (DBQuery.getQueryResultSet().next()) return true;
        else return false;
    }

    @FXML
        public void changeSceneMainWindowView(ActionEvent event) throws SQLException, IOException {
        HelperQuery.getCustomerData();
        HelperQuery.getAppointmentData();
        NewWindow.display((Stage) loginWindowPane.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void loginButtonClickEvent(ActionEvent event) throws IOException, SQLException {
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            if (determineIfUserExists(usernameTextField.getText(), passwordTextField.getText())) {
                System.out.println("user found");
                loginInvalidWarningText.setText("");
                changeSceneMainWindowView(event);
            } else {
                System.out.println("user not found");
                loginInvalidWarningText.setText("Username and Password combination is invalid.");
            }
        } else loginInvalidWarningText.setText("Username and Password cannot be empty.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
