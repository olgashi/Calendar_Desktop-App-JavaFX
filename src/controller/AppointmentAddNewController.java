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
    private Text appointmentAddNewMainWindowLabel;
    @FXML
    private Text newAppointmentTitleText;
    @FXML
    private Text newAppointmentDateText;
    @FXML
    private Text newAppointmentTimeText;
    @FXML
    private Text newAppointmentCustomerText;
    @FXML
    private Text newAppointmentLocationText;
    @FXML
    private Text newAppointmentTypeText;
    @FXML
    private Text newAppointmentDescriptionText;
    @FXML
    private Text newAppointmentDurationText;
    @FXML
    private Text newAppointmentContactText;
    @FXML
    private ComboBox newAppointmentDurationComboBox;
    @FXML
    private ComboBox newAppointmentHoursComboBox;
    @FXML
    private ComboBox newAppointmentMinutesComboBox;
    @FXML
    private DatePicker addNewAppointmentDatePicker;
    @FXML
    private TextField addNewAppointmentTimeHoursTextField;
    @FXML
    private TextField addNewAppointmentTimeMinutesTextField;
    @FXML
    private ComboBox addNewAppointmentTypeComboBox;
    @FXML
    private ComboBox addNewAppointmentContactComboBox;
    @FXML
    private TextField addNewAppointmentDescriptionTextField;
    @FXML
    private TextField addNewAppointmentTitleTextField;
    @FXML
    private TextField addNewAppointmentLocationTextField;
    @FXML
    private TableView<Customer> addNewAppointmentCustomerTable;
    @FXML
    private TableColumn<Customer,String> addNewAppointmentCustomerNameColumn;
    @FXML
    private TableColumn<Customer,String> addNewAppointmentCustomerLocationColumn;
    @FXML
    private TableColumn<Customer,String> addNewAppointmentCustomerPhoneNumberColumn;
    @FXML
    private Button addNewAppointmentCancelButton;
    @FXML
    private Button addNewAppointmentCreateButton;
    private Customer selectedCustomer;
    private int selectedCustomerId, userId;
    private String  contact, url;
    String loggedInUserName = User.getUserName();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public void createAppointment(ActionEvent event) throws SQLException, IOException {
        LocalDateTime fullAppointmentStartDateTime, fullAppointmentEndDateTime;
        selectedCustomer = addNewAppointmentCustomerTable.getSelectionModel().getSelectedItem();
        String durationTempStr = newAppointmentDurationComboBox.getValue().toString();
        String durationTempArr[]= durationTempStr.split(" ");
        int appointmentDuration = Integer.parseInt(durationTempArr[0]);

        if (InputValidation.checkForAnyEmptyInputs(addNewAppointmentDescriptionTextField, addNewAppointmentTitleTextField, addNewAppointmentLocationTextField)) {
            AlertMessage.display("All fields are required. Please make changes and try again.", "warning");
            return;
        }
        if (addNewAppointmentTypeComboBox.getValue() == null) {
            AlertMessage.display("Please select appointment type", "warning");
        }
        if (addNewAppointmentDatePicker.getValue() == null) {
            AlertMessage.display("Date cannot be empty", "warning");
            return;
        }
        if (addNewAppointmentContactComboBox.getValue() == null) {
            AlertMessage.display("Contact cannot be empty", "warning");
            return;
        }
        if (newAppointmentHoursComboBox.getValue() == null && newAppointmentMinutesComboBox.getValue() == null) {
            AlertMessage.display("Please specify time for the appointment", "warning");
            return;
        }
        if (newAppointmentHoursComboBox.getValue() == null) {
            AlertMessage.display("Please select hours for the appointment", "warning");
            return;
        }
        if (newAppointmentMinutesComboBox.getValue() == null) {
            AlertMessage.display("Please select minutes for the appointment", "warning");
            return;
        }

        if (selectedCustomer == null) {
            AlertMessage.display("Please select a customer for this appointment.", "warning");
            return;
        }
        else {
            fullAppointmentStartDateTime = LocalDateTime.of(
                    addNewAppointmentDatePicker.getValue().getYear(),
                    addNewAppointmentDatePicker.getValue().getMonthValue(),
                    addNewAppointmentDatePicker.getValue().getDayOfMonth(),
                    Integer.parseInt(newAppointmentHoursComboBox.getValue().toString()),
                    Integer.parseInt(newAppointmentMinutesComboBox.getValue().toString()));

            fullAppointmentStartDateTime = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s").format(fullAppointmentStartDateTime), dtf);
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(Duration.ofMinutes(appointmentDuration));
            selectedCustomerId = Integer.parseInt(selectedCustomer.getCustomerId());

            if (Schedule.overlappingAppointmentsCheck(fullAppointmentStartDateTime, selectedCustomerId, Integer.parseInt(Schedule.setAppointmentId()))){
                AlertMessage.display("Creating overlapping appointments is not allowed, please select different time and try again", "warning");
                return;
            }
            if (!Schedule.outsideOfBusinessHoursCheck(fullAppointmentStartDateTime, fullAppointmentEndDateTime)){
                AlertMessage.display("Scheduling appointments outside of business hours is not allowed, please select different time and try again", "warning");
                return;
            }

//            TODO change these values to actual values
            userId = 1;
            contact = addNewAppointmentContactComboBox.getValue().toString();
            url = "test";

            createAppointmentDB(fullAppointmentStartDateTime, fullAppointmentEndDateTime);

            if (DBQuery.queryNumRowsAffected() > 0) {
                addAppointmentToSchedule(fullAppointmentStartDateTime, fullAppointmentEndDateTime);
                AlertMessage.display("Appointment was created successfully!", "information");
                loadMainWindowAppointmentAddNew(event);
            }
            else AlertMessage.display("There was a problem creating an appointment", "warning");
        }
    }

    public void addAppointmentToSchedule(LocalDateTime fullAppointmentStartDateTime, LocalDateTime fullAppointmentEndDateTime) {
        Schedule.addAppointment(new Appointment(Schedule.setAppointmentId(), addNewAppointmentTitleTextField.getText(), addNewAppointmentDescriptionTextField.getText(),
                addNewAppointmentLocationTextField.getText(), contact, addNewAppointmentTypeComboBox.getValue().toString(), url, fullAppointmentStartDateTime.format(dtf),
                fullAppointmentEndDateTime.format(dtf), selectedCustomer.getCustomerId(), selectedCustomer.getCustomerName()));
    }

    public void createAppointmentDB(LocalDateTime fullAppointmentStartDateTime, LocalDateTime fullAppointmentEndDateTime) throws SQLException {
        DBQuery.createQuery("INSERT INTO appointment (customerId, userId, title, description, location, contact, " +
                "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values(" +
                "'" + selectedCustomerId + "'" + ", " + "'" + userId + "'" + ", " + "'" + addNewAppointmentTitleTextField.getText() + "'" + ", " +
                "'" + addNewAppointmentDescriptionTextField.getText() +"'" + ", " + "'" + addNewAppointmentLocationTextField.getText() + "'" + ", " +
                "'" + contact + "'" + ", "+ "'" + addNewAppointmentTypeComboBox.getValue().toString() + "'" + ", " + "'" + url + "'" + ", " + "'" +
                DateTimeUtils.convertToUTCTime(fullAppointmentStartDateTime) + "'" + ", " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentEndDateTime) +
                "'" + ", " + "'" + LocalDateTime.now() + "'"+ ", "+ "'" + loggedInUserName + "'" + ", " +
                "'" + LocalDateTime.now() + "'" + ", " + "'"+ loggedInUserName +"'"+")");
    }

    //TODO am pm is not considered when adding appointment, add
//    TODO add check that time entered is within business hours and date entered is not on the weekend
    @FXML
    private void loadMainWindowAppointmentAddNew(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<DatePicker, DateCell> dayCellFactory= Calendar.customDayCellFactory();
        addNewAppointmentDatePicker.setDayCellFactory(dayCellFactory);

        newAppointmentDurationComboBox.getItems().addAll("15 mins", "30 mins", "45 mins", "60 mins");
        addNewAppointmentTypeComboBox.getItems().addAll(Reports.allExistingAppointmentTypes());
        addNewAppointmentContactComboBox.getItems().addAll(Reports.allExistingConsultants());
        newAppointmentHoursComboBox.getItems().addAll("09","10", "11", "12", "13", "14", "15", "16", "17");
        newAppointmentMinutesComboBox.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
        addNewAppointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerName"));
        addNewAppointmentCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerCity"));
        addNewAppointmentCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerPhoneNumber"));
        addNewAppointmentCustomerTable.setItems(Customer.getCustomerList());
    }
}
// TODO include info popups on hover for input fields
