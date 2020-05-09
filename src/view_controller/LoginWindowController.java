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
import model.User;
import utilities.DBQuery;
import utilities.HelperQuery;
import utilities.LoginLanguage;
import utilities.NewWindow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
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
    private Text loginWindowLabelText;
    @FXML
    private Text loginUsernameText;
    @FXML
    private Text loginPasswordText;
    @FXML
    private Pane loginWindowPane;

    public static User loggedInUser;
    Locale currentLocale;

    public boolean determineIfUserExists(String userN, String pass) throws SQLException {
        DBQuery.createQuery("SELECT userId, userName FROM user WHERE userName = " + "'" + userN + "'" + " AND password = " + "'" + pass + "'");
        if (DBQuery.getQueryResultSet().next()) return true;
        else return false;
    }

    @FXML
        public void changeSceneMainWindowView(ActionEvent event) throws SQLException, IOException {
        HelperQuery.getCustomerData();
        HelperQuery.getAppointmentData();
        NewWindow.display((Stage) loginWindowPane.getScene().getWindow(), getClass().getResource("MainWindow.fxml"));
    }

    public void loginButtonClickEvent(ActionEvent event) throws IOException, SQLException {
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            if (determineIfUserExists(usernameTextField.getText(), passwordTextField.getText())) {
                System.out.println("user found");
                loggedInUser = new User(DBQuery.getQueryResultSet().getString("userName"), DBQuery.getQueryResultSet().getString("userId"));
                changeSceneMainWindowView(event);
            } else {
                System.out.println("user not found");
                LoginLanguage.userNamePassInvalidComboMessage(currentLocale.getCountry(), loginInvalidWarningText);
            }
        } else LoginLanguage.userNamePassEmptyMessage(currentLocale.getCountry(), loginInvalidWarningText);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginInvalidWarningText.setText("");
        currentLocale = Locale.getDefault();
        System.out.println("current locale: " + currentLocale.getCountry());
        LoginLanguage.setLoginWindowLabels(currentLocale.getCountry(), loginUsernameText, loginPasswordText, loginWindowLabelText, loginButton );
    }
}
