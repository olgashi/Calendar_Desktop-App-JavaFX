
package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utilities.AlertMessage;
import utilities.AuditLog;
import utilities.NewWindow;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private AnchorPane mainWindowView;
    @FXML
    private Button customersMainWindowViewButton;
    @FXML
    private Button appointmentsMainWindowViewButton;
    @FXML
    private Button calendarMainWindowViewButton;
    @FXML
    private Button reportsMainWindowViewButton;
    @FXML
    private Button exitButton;

    @FXML
    public void exitButtonClickEvent(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void loadCustomersWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("/view/CustomersMainWindow.fxml"));
    }

    @FXML
    private void loadAppointmentsWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("/view/AppointmentsMainWindow.fxml"));
    }

    @FXML
    private void loadCalendarWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("/view/CalendarMainWindow.fxml"));
    }

    @FXML
    private void loadReportsWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("/view/ReportsMainWindow.fxml"));
    }
    public void initMainWindowData(Appointment upcomingAppt, User user){
        AuditLog.addLog(user);
        if (upcomingAppt != null) AlertMessage.display("There is an upcoming appointment with " +
                upcomingAppt.getAppointmentCustomerName() + " within 15 minutes", "warning");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
