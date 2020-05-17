
package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;
import model.User;
import utilities.AlertMessage;
import utilities.AuditLog;
import utilities.NewWindow;
import utilities.Reports;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
//TODO populate database with fun/realistic names and places before submission

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
    private void loadCustomersWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @FXML
    private void loadAppointmentsWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("AppointmentsMainWindow.fxml"));
    }

    @FXML
    private void loadCalendarWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("CalendarMainWindow.fxml"));
    }

//uncomment when reports view and functionality are created
    @FXML
    private void loadReportsWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("ReportsMainWindow.fxml"));
    }
    public void initMainWindowData(Appointment upcomingAppt, User user){
        AuditLog.addLog(user);
        if (upcomingAppt != null) AlertMessage.display("There is an upcoming appointment with " +
                upcomingAppt.getAppointmentCustomerName() + " within 15 minutes", "warning");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println(Reports.typesByMonth(LocalDateTime.parse("2020-05-10 10:00:00.0", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s"))));

    }
}
