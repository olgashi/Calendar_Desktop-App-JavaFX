package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Customer;
import model.Schedule;
import model.User;
import utilities.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentAddNewController implements Initializable {
    @FXML
    private Text apptAddNewMainWindowLabel;
    @FXML
    private Text newApptTitleText;
    @FXML
    private Text newApptmentDateText;
    @FXML
    private Text newApptTimeText;
    @FXML
    private Text newApptLocationText;
    @FXML
    private Text newApptTypeText;
    @FXML
    private Text newApptDescriptionText;
    @FXML
    private Text newApptDurationText;
    @FXML
    private Text newApptContactText;
    @FXML
    private ComboBox<String> addNewApptDurationComboBox;
    @FXML
    private ComboBox<String> addNewApptHoursComboBox;
    @FXML
    private ComboBox<String> addNewApptMinutesComboBox;
    @FXML
    private DatePicker addNewApptDatePicker;
    @FXML
    private ComboBox<String> addNewApptTypeComboBox;
    @FXML
    private ComboBox<String> addNewApptContactComboBox;
    @FXML
    private TextField addNewApptDescriptionTextField;
    @FXML
    private TextField addNewApptTitleTextField;
    @FXML
    private TextField addNewApptLocationTextField;
    @FXML
    private TableView<Customer> addNewApptCustomerTable;
    @FXML
    private TableColumn<Customer,String> addNewApptCustomerNameColumn;
    @FXML
    private TableColumn<Customer,String> addNewApptCustomerLocationColumn;
    @FXML
    private TableColumn<Customer,String> addNewApptCustomerPhoneNumberColumn;
    @FXML
    private Button addNewApptCancelButton;
    @FXML
    private Button addNewApptCreateButton;
    private Customer selectedCustomer;
    private int selectedCustomerId;
    private int userId = 1;
    private String  contact;
    private String url = "not provided";
    private String loggedInUserName = User.getUserName();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public void createAppointment(ActionEvent event) throws SQLException, IOException {
        LocalDateTime fullApptStartDateTime, fullApptEndDateTime;
        selectedCustomer = addNewApptCustomerTable.getSelectionModel().getSelectedItem();

        if (InputValidation.checkForAnyEmptyInputs(addNewApptDescriptionTextField, addNewApptTitleTextField, addNewApptLocationTextField)) {
            AlertMessage.display("All fields are required. Please make changes and try again.", "warning");
            return;
        }
        if (addNewApptTypeComboBox.getValue() == null) {
            AlertMessage.display("Please select appointment type.", "warning");
        }
        if (addNewApptDatePicker.getValue() == null) {
            AlertMessage.display("Date cannot be empty.", "warning");
            return;
        }
        if (addNewApptContactComboBox.getValue() == null) {
            AlertMessage.display("Contact cannot be empty.", "warning");
            return;
        }
        if (addNewApptHoursComboBox.getValue() == null && addNewApptMinutesComboBox.getValue() == null) {
            AlertMessage.display("Please specify time for the appointment.", "warning");
            return;
        }
        if (addNewApptHoursComboBox.getValue() == null) {
            AlertMessage.display("Please select hours for the appointment.", "warning");
            return;
        }
        if (addNewApptMinutesComboBox.getValue() == null) {
            AlertMessage.display("Please select minutes for the appointment.", "warning");
            return;
        }
        if (selectedCustomer == null) {
            AlertMessage.display("Please select a customer for this appointment.", "warning");
            return;
        }
        else {
            fullApptStartDateTime = LocalDateTime.of(
                    addNewApptDatePicker.getValue().getYear(),
                    addNewApptDatePicker.getValue().getMonthValue(),
                    addNewApptDatePicker.getValue().getDayOfMonth(),
                    Integer.parseInt(addNewApptHoursComboBox.getValue()),
                    Integer.parseInt(addNewApptMinutesComboBox.getValue()));

            String durationTempStr = addNewApptDurationComboBox.getValue();
            String durationTempArr[]= durationTempStr.split(" ");
            int apptDuration = Integer.parseInt(durationTempArr[0]);

            fullApptStartDateTime = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s").format(fullApptStartDateTime), dtf);
            fullApptEndDateTime = fullApptStartDateTime.plus(Duration.ofMinutes(apptDuration));

            selectedCustomerId = Integer.parseInt(selectedCustomer.getCustomerId());
            contact = addNewApptContactComboBox.getValue();

            if (Schedule.overlappingAppointmentsCheck(fullApptStartDateTime, fullApptEndDateTime)){
                AlertMessage.display("Creating overlapping appointments is not allowed, please select different time and try again.", "warning");
                return;
            }
            if(!Schedule.checkIfWithinNormalBusinessHours(fullApptStartDateTime, fullApptEndDateTime)) {
                AlertMessage.display("The appointment is outside of business hours please correct and try again.", "warning");
                return;
            }
            createAppointmentDB(fullApptStartDateTime, fullApptEndDateTime);
            if (DBQuery.queryNumRowsAffected() > 0) {
                addAppointmentToSchedule(fullApptStartDateTime, fullApptEndDateTime);
                AlertMessage.display("Appointment was created successfully!", "information");
                loadMainWindowAppointmentAddNew(event);
            }
            else AlertMessage.display("There was a problem creating an appointment.", "warning");
        }
    }

    public void addAppointmentToSchedule(LocalDateTime fullAppointmentStartDateTime, LocalDateTime fullAppointmentEndDateTime) {
        Schedule.addAppointment(new Appointment(Schedule.setAppointmentId(), addNewApptTitleTextField.getText(), addNewApptDescriptionTextField.getText(),
                addNewApptLocationTextField.getText(), contact, addNewApptTypeComboBox.getValue(), url, fullAppointmentStartDateTime.format(dtf),
                fullAppointmentEndDateTime.format(dtf), selectedCustomer.getCustomerId(), selectedCustomer.getCustomerName()));
    }

    public void createAppointmentDB(LocalDateTime fullAppointmentStartDateTime, LocalDateTime fullAppointmentEndDateTime) throws SQLException {
        DBQuery.createQuery("INSERT INTO appointment (customerId, userId, title, description, location, contact, " +
                "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values(" +
                "'" + selectedCustomerId + "'" + ", " + "'" + userId + "'" + ", " + "'" + addNewApptTitleTextField.getText() + "'" + ", " +
                "'" + addNewApptDescriptionTextField.getText() +"'" + ", " + "'" + addNewApptLocationTextField.getText() + "'" + ", " +
                "'" + contact + "'" + ", "+ "'" + addNewApptTypeComboBox.getValue() + "'" + ", " + "'" + url + "'" + ", " + "'" +
                DateTimeUtils.convertToUTCTime(fullAppointmentStartDateTime) + "'" + ", " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentEndDateTime) +
                "'" + ", " + "'" + LocalDateTime.now() + "'"+ ", "+ "'" + loggedInUserName + "'" + ", " +
                "'" + LocalDateTime.now() + "'" + ", " + "'"+ loggedInUserName +"'"+")");
    }

    @FXML
    private void loadMainWindowAppointmentAddNew(ActionEvent event) throws IOException {
        NewWindow.display((Stage) apptAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<DatePicker, DateCell> dayCellFactory= Calendar.customDayCellFactory();
        addNewApptDatePicker.setDayCellFactory(dayCellFactory);

        addNewApptDurationComboBox.getItems().addAll("15 mins", "30 mins", "45 mins", "60 mins");
        addNewApptTypeComboBox.getItems().addAll(Reports.allExistingAppointmentTypes());
        addNewApptContactComboBox.getItems().addAll(Reports.allExistingConsultants());
        addNewApptHoursComboBox.getItems().addAll("09","10", "11", "12", "13", "14", "15", "16");
        addNewApptMinutesComboBox.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

        addNewApptCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addNewApptCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        addNewApptCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        addNewApptCustomerTable.setItems(Customer.getCustomerList());
    }
}
