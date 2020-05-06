package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import utilities.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

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
    private DatePicker addNewAppointmentDatePicker;
    @FXML
    private RadioButton addNewAppointmentTimeAM;
    @FXML
    private RadioButton addNewAppointmentTimePM;
    @FXML
    private ToggleGroup addNewAppointmentAmPMtoggleGroup;
    @FXML
    private TextField addNewAppointmentTimeHoursTextField;
    @FXML
    private TextField addNewAppointmentTimeMinutesTextField;
    @FXML
    private TextField addNewAppointmentTypeTextField;
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
    private String aTitle,aDate, aTimeHours, aTimeMinutes, aLocation, aType, aDescription, contact, url;;
//            TODO add check for conflicting appointment
//            TODO add end time
//            TODO add "information" to alertmessage.display
//    TODO user should not be able to add appointments outside business hours and on the weekends
//    TODO add length of the appointment

    public void createAppointment(ActionEvent event) throws SQLException, IOException {
        LocalDateTime fullAppointmentStartDateTime, fullAppointmentEndDateTime;
        selectedCustomer = addNewAppointmentCustomerTable.getSelectionModel().getSelectedItem();

        if (!InputValidation.checkForEmptyInputs(addNewAppointmentTimeHoursTextField, addNewAppointmentTimeMinutesTextField, addNewAppointmentTypeTextField,
                addNewAppointmentDescriptionTextField, addNewAppointmentTitleTextField, addNewAppointmentLocationTextField)) {
            AlertMessage.display("All fields are required. Please make try again.", "warning");
            return;
        }
        if (addNewAppointmentDatePicker.getValue() == null) {
            AlertMessage.display("Date cannot be empty", "warning");
            return;
        }
        if (!InputValidation.timeInputNumbersOnly(addNewAppointmentTimeHoursTextField, addNewAppointmentTimeMinutesTextField)) {
            AlertMessage.display("Time has to be numbers only. Please correct and try again.", "warning");
            return;
        }
        if (!InputValidation.timeInputProperLength(addNewAppointmentTimeHoursTextField, addNewAppointmentTimeMinutesTextField)) {
            AlertMessage.display("Time has to be numbers only, not longer than 2 digits. Please correct and try again.", "warning");
            return;
        }
        if (selectedCustomer == null) {
            AlertMessage.display("Please select a customer for this appointment.", "warning");
            return;
        } else {
            fullAppointmentStartDateTime = LocalDateTime.of(
                    addNewAppointmentDatePicker.getValue().getYear(),
                    addNewAppointmentDatePicker.getValue().getMonthValue(),
                    addNewAppointmentDatePicker.getValue().getDayOfMonth(),
                    Integer.parseInt(addNewAppointmentTimeHoursTextField.getText()),
                    Integer.parseInt(addNewAppointmentTimeMinutesTextField.getText()));

            // create appointment
            selectedCustomerId = Integer.parseInt(selectedCustomer.getCustomerId());
//            TODO change these values to actual values
            userId = 1;
            fullAppointmentEndDateTime = fullAppointmentStartDateTime;
            contact = "test";
            url = "test";
            DBQuery.createQuery("INSERT INTO appointment (customerId, userId, title, description, location, contact, " +
                    "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values(" +
                    "'" + selectedCustomerId + "'" + ", " + "'" + userId + "'" + ", " + "'" + addNewAppointmentTitleTextField.getText() + "'" + ", " +
                    "'" + addNewAppointmentDescriptionTextField.getText() +"'" + ", " + "'" + addNewAppointmentLocationTextField.getText() + "'" + ", " +
                    "'" + "test" + "'" + ", "+ "'" + addNewAppointmentTypeTextField.getText() + "'" + ", " +
                    "'" + url + "'" + ", " + "'" + fullAppointmentStartDateTime + "'" + ", " + "'" + fullAppointmentEndDateTime + "'" +
                    ", " + "'" + LocalDateTime.now() + "'"+ ", 'admin', " + "'" + LocalDateTime.now() + "'" + ", 'admin')");
            if (DBQuery.queryNumRowsAffected() > 0) loadMainWindowAppointmentAddNew(event);
            else AlertMessage.display("There was a problem creating an appointment", "warning");
        }
    }
//TODO am pm is not considered when adding appointment, add
//    TODO add check that time entered is within business hours and date entered is not on the weekend
    @FXML
    private void loadMainWindowAppointmentAddNew(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addNewAppointmentAmPMtoggleGroup = new ToggleGroup();
        this.addNewAppointmentTimeAM.setToggleGroup(addNewAppointmentAmPMtoggleGroup);
        this.addNewAppointmentTimePM.setToggleGroup(addNewAppointmentAmPMtoggleGroup);
        addNewAppointmentTimeAM.setSelected(true);

////        loadCustomerTableData();
        Customer.getCustomerList();
        addNewAppointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerName"));
        addNewAppointmentCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerCity"));
        addNewAppointmentCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerPhoneNumber"));
        addNewAppointmentCustomerTable.setItems(Customer.getCustomerList());
    }
}
// TODO include info popups on hover for input fields
