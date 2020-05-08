package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import utilities.AlertMessage;
import utilities.InputValidation;
import utilities.NewWindow;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentModifyController implements Initializable {

    @FXML
    private Text appointmentModifyMainWindowLabel;
    @FXML
    private Text modifyAppointmentTitleText;
    @FXML
    private Text modifyAppointmentDateText;
    @FXML
    private Text modifyAppointmentTimeText;

    @FXML
    private Text modifyAppointmentLocationText;
    @FXML
    private Text modifyAppointmentTypeText;
    @FXML
    private Text modifyAppointmentDescriptionText;
    @FXML
    private Text modifyAppointmentDatePickerCurrentDateText;


    @FXML
    private Text existingAppointmentTitleText;
    @FXML
    private Text existingAppointmentDateText;
    @FXML
    private Text existingAppointmentTimeText;
    @FXML
    private Text existingAppointmentLocationText;
    @FXML
    private Text existingAppointmentTypeText;
    @FXML
    private Text existingAppointmentDescriptionText;
    @FXML
    private Text existingAppointmentCustomerText;

    @FXML
    private Text existingAppointmentTitleValue;
    @FXML
    private Text existingAppointmentDateValue;
    @FXML
    private Text existingAppointmentTimeValue;
    @FXML
    private Text existingAppointmentLocationValue;
    @FXML
    private Text existingAppointmentTypeValue;
    @FXML
    private Text existingAppointmentDescriptionValue;
    @FXML
    private Text existingAppointmentCustomerValue;


    @FXML
    private DatePicker modifyAppointmentNewDate;
    @FXML
    private RadioButton modifyAppointmentTimeAM;
    @FXML
    private RadioButton modifyAppointmentTimePM;
    @FXML
    private ToggleGroup modifyAppointmentAmPMtoggleGroup;
    @FXML
    private TextField modifyAppointmentTimeHoursTextField;
    @FXML
    private TextField modifyAppointmentTimeMinutesTextField;
    @FXML
    private TextField modifyAppointmentTypeTextField;
    @FXML
    private TextField modifyAppointmentDescriptionTextField;
    @FXML
    private TextField modifyAppointmentTitleTextField;
    @FXML
    private TextField modifyAppointmentLocationTextField;
    @FXML
    private TableView<Customer> modifyAppointmentCustomerTable;
    @FXML
    private TableColumn<Customer,String> modifyAppointmentCustomerNameColumn;
    @FXML
    private TableColumn<Customer,String> modifyAppointmentCustomerLocationColumn;
    @FXML
    private TableColumn<Customer,String> modifyAppointmentCustomerPhoneNumberColumn;
    @FXML
    private Button modifyAppointmentCancelButton;
    @FXML
    private Button modifyAppointmentCreateButton;
    private Appointment selectedAppointment;
    private Customer selectedCustomer;
    private int selectedCustomerId, userId;

    public void initModifyAppointmentData(Appointment appointment) {
        String appointmentStartDateTime = appointment.getAppointmentStart();
//        TODO refactor
        String dateTimeArr[] = appointmentStartDateTime.split(" ");
        String appointmentStartDate = dateTimeArr[0];
        String appointmentStartTime = dateTimeArr[1];
        String yearDayMonthArr[] = appointmentStartDate.split("-");
        String year = yearDayMonthArr[0];
        String day = yearDayMonthArr[2];
        String hourMinArr[] = appointmentStartTime.split(":");
        String month = yearDayMonthArr[1];
        String hour = hourMinArr[0];
        String minute = hourMinArr[1];

        selectedAppointment = appointment;
        existingAppointmentTitleValue.setText(selectedAppointment.getAppointmentTitle());
        existingAppointmentDateValue.setText(appointmentStartDate);
        existingAppointmentTimeValue.setText(appointmentStartTime);
        existingAppointmentLocationValue.setText(selectedAppointment.getAppointmentLocation());
        existingAppointmentTypeValue.setText(selectedAppointment.getAppointmentType());
        existingAppointmentDescriptionValue.setText(selectedAppointment.getAppointmentDescription());
        existingAppointmentCustomerValue.setText(selectedAppointment.getAppointmentCustomerName() + ", " + selectedAppointment.getAppointmentLocation());
    }

    public void updateAppointment(ActionEvent event) throws SQLException, IOException {
        Customer selectedAppointmentCustomer = modifyAppointmentCustomerTable.getSelectionModel().getSelectedItem();

        if (!InputValidation.checkForAnyEmptyInputs(modifyAppointmentTimeHoursTextField, modifyAppointmentTimeMinutesTextField, modifyAppointmentTypeTextField,
                modifyAppointmentDescriptionTextField, modifyAppointmentTitleTextField, modifyAppointmentLocationTextField, modifyAppointmentTimeHoursTextField,
                modifyAppointmentTimeMinutesTextField) && (selectedAppointmentCustomer == null)) {
            AlertMessage.display("Please provide new values for an appointment and try again.", "warning");
            return;
        }

        if (!modifyAppointmentTimeHoursTextField.getText().isEmpty()) {
            if (!InputValidation.timeInputNumbersOnly(modifyAppointmentTimeHoursTextField)) {
                AlertMessage.display("Hours field has to be numbers only. Please correct and try again.", "warning");
                return;
            } else {
                if (!InputValidation.timeInputProperLength(modifyAppointmentTimeHoursTextField)) {
                    AlertMessage.display("Hours value should be not longer than 2 digits. Please correct and try again.", "warning");
                    return;
                }
            }
        }
        if (!modifyAppointmentTimeMinutesTextField.getText().isEmpty()) {
            if (!InputValidation.timeInputNumbersOnly(modifyAppointmentTimeMinutesTextField)) {
                AlertMessage.display("Minutes field has to be numbers only. Please correct and try again.", "warning");
                return;
            } else {
                if (!InputValidation.timeInputProperLength(modifyAppointmentTimeMinutesTextField)) {
                    AlertMessage.display("Minutes value should be not longer than 2 digits. Please correct and try again.", "warning");
                    return;
                }
            }
        }
        AlertMessage.display("fields look good", "warning");


    }

    @FXML
    private void loadMainWindowAppointmentModify(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        TODO extract this to a method?
        modifyAppointmentAmPMtoggleGroup = new ToggleGroup();
        this.modifyAppointmentTimeAM.setToggleGroup(modifyAppointmentAmPMtoggleGroup);
        this.modifyAppointmentTimePM.setToggleGroup(modifyAppointmentAmPMtoggleGroup);
        modifyAppointmentTimeAM.setSelected(true);

        modifyAppointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerName"));
        modifyAppointmentCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerCity"));
        modifyAppointmentCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerPhoneNumber"));
        modifyAppointmentCustomerTable.setItems(Customer.getCustomerList());
    }
}
