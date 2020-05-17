package view_controller;
//TODO: style with css when finished
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;
import model.User;
import utilities.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
        public void loadDataFromDB() throws SQLException {
        LoadDataQuery.getCustomerData();
        LoadDataQuery.getAppointmentData();
    }

    public void loginButtonClickEvent(ActionEvent event) throws IOException, SQLException {
        Appointment upcomingAppointments = null;
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            if (determineIfUserExists(usernameTextField.getText(), passwordTextField.getText())) {
                loggedInUser = new User(DBQuery.getQueryResultSet().getString("userName"), DBQuery.getQueryResultSet().getString("userId"));
                loadDataFromDB();
                LocalDateTime userLoginTime = LocalDateTime.now();
                if (Schedule.appointmentsWithinFifteenMinutes(userLoginTime) != null) {
                    upcomingAppointments = Schedule.appointmentsWithinFifteenMinutes(userLoginTime);
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("MainWindow.fxml"));
                Parent mainViewParent = loader.load();
                Scene mainWindowView = new Scene(mainViewParent);
                MainWindowController controller = loader.getController();
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainWindowView);
                window.show();
                controller.initMainWindowData(upcomingAppointments, loggedInUser);
            } else {
                LoginLanguage.userNamePassInvalidComboMessage(currentLocale.getCountry(), loginInvalidWarningText);
            }
        } else LoginLanguage.userNamePassEmptyMessage(currentLocale.getCountry(), loginInvalidWarningText);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AuditLog.createFile();
        loginInvalidWarningText.setText("");
        currentLocale = Locale.getDefault();
        LoginLanguage.setLoginWindowLabels(currentLocale.getCountry(), loginUsernameText, loginPasswordText, loginWindowLabelText, loginButton );
    }
}
