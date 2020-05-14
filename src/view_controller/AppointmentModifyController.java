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
import model.Schedule;
import model.User;
import utilities.*;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
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
    private Text existingAppointmentDurationText;
    @FXML
    private Text existingAppointmentDurationValue;


    @FXML
    private Text modifyAppointmentDurationText;
    @FXML
    private ComboBox modifyAppointmentDurationComboBox;
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
//    TODO these variables are a mess, rename/refactor
    private String updatedCustomerId, updatedTitle, updatedHours, updatedMinutes, updatedAppointmentYear,
        updatedAppointmentDay, updatedLocation, updatedType, updatedDescription;
    private Month updatedAppointmentMonth;
    private String existingAppointmentMonth;
    private String existingAppointmentYear, existingAppointmentDay, existingAppointmentHours,
            existingAppointmentMinutes, existingAppointmentStartDate, existingAppointmentStartTime, appointmentStartDateTime;
    String appointmentEndDateTime;
    String loggedInUserName = User.getUserName();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void initModifyAppointmentData(Appointment appointment) {
        selectedAppointment = appointment;
        appointmentStartDateTime = selectedAppointment.getAppointmentStart();
        appointmentEndDateTime = selectedAppointment.getAppointmentEnd();

        String dateTimeArr[] = appointmentStartDateTime.split(" |T");
        existingAppointmentStartDate = dateTimeArr[0];
        existingAppointmentStartTime = dateTimeArr[1];
        // get existing appointment start year, day, month
        String yearDayMonthArr[] = existingAppointmentStartDate.split("-");
        existingAppointmentYear = yearDayMonthArr[0];
        existingAppointmentDay = yearDayMonthArr[2];

        String temp[] = existingAppointmentStartDate.split("-");
        int mnth = Integer.parseInt(temp[1]);
        existingAppointmentMonth = new DateFormatSymbols().getMonths()[mnth-1];
        // get existing appointment start year, day, month
        String hourMinArr[] = existingAppointmentStartTime.split(":");
        existingAppointmentHours = hourMinArr[0];
        existingAppointmentMinutes = hourMinArr[1];

        existingAppointmentTitleValue.setText(selectedAppointment.getAppointmentTitle());
        existingAppointmentDateValue.setText(existingAppointmentStartDate);
        existingAppointmentTimeValue.setText(existingAppointmentStartTime);
//        existingAppointmentDurationValue.setText();
        existingAppointmentLocationValue.setText(selectedAppointment.getAppointmentLocation());
        existingAppointmentTypeValue.setText(selectedAppointment.getAppointmentType());
        existingAppointmentDescriptionValue.setText(selectedAppointment.getAppointmentDescription());
        existingAppointmentCustomerValue.setText(selectedAppointment.getAppointmentCustomerName());
    }

    public void updateAppointment(ActionEvent event) throws SQLException, IOException {
        Customer selectedAppointmentCustomer = modifyAppointmentCustomerTable.getSelectionModel().getSelectedItem();
        LocalDateTime fullAppointmentStartDateTime;
        LocalDateTime fullAppointmentEndDateTime;
        LocalDateTime start = LocalDateTime.parse(appointmentStartDateTime, dtf);
        LocalDateTime end = LocalDateTime.parse(appointmentEndDateTime, dtf);
        Duration existingApptDuration = Duration.between(start, end);

//        System.out.println("dur :" + existingApptDuration);
//        long existingApptDurationMinutes = existingApptDuration.toMinutes();
//        Duration appointmentDuration;
//        if (modifyAppointmentDurationComboBox.getValue() != null) {
//            String durationTempStr = modifyAppointmentDurationComboBox.getValue().toString();
//            String durationTempArr[] = durationTempStr.split(" ");
//            appointmentDuration = Duration.ofMinutes(existingApptDurationMinutes);
//        } else {
//            appointmentDuration = Duration.ofMinutes(0);
//        }
//        System.out.println(appointmentDuration);
        LocalDateTime existingAppointmentEnd = LocalDateTime.parse(appointmentEndDateTime, dtf);
        if (!InputValidation.checkForAnyEmptyInputs(modifyAppointmentTimeHoursTextField, modifyAppointmentTimeMinutesTextField, modifyAppointmentTypeTextField,
                modifyAppointmentDescriptionTextField, modifyAppointmentTitleTextField, modifyAppointmentLocationTextField, modifyAppointmentTimeHoursTextField,
                modifyAppointmentTimeMinutesTextField) && selectedAppointmentCustomer == null && modifyAppointmentNewDate.getValue() == null &&
                modifyAppointmentDurationComboBox.getValue() == null) {
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
                } else {
                    existingAppointmentHours = modifyAppointmentTimeHoursTextField.getText();

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
                } else {
                    existingAppointmentMinutes = modifyAppointmentTimeMinutesTextField.getText();
                }
            }
        }


        updatedTitle = modifyAppointmentTitleTextField.getText().isEmpty() ? selectedAppointment.getAppointmentTitle() : modifyAppointmentTitleTextField.getText();
        updatedLocation = modifyAppointmentLocationTextField.getText().isEmpty() ? selectedAppointment.getAppointmentLocation() : modifyAppointmentLocationTextField.getText();
        updatedType = modifyAppointmentTypeTextField.getText().isEmpty() ? selectedAppointment.getAppointmentType() : modifyAppointmentLocationTextField.getText();
        updatedDescription = modifyAppointmentDescriptionTextField.getText().isEmpty() ? selectedAppointment.getAppointmentDescription() : modifyAppointmentDescriptionTextField.getText();
        updatedCustomerId = selectedAppointmentCustomer == null ? selectedAppointment.getAppointmentCustomerId() : selectedAppointmentCustomer.getCustomerId();
        updatedHours = modifyAppointmentTimeHoursTextField.getText().isEmpty() ? existingAppointmentHours : modifyAppointmentTimeHoursTextField.getText();
        updatedMinutes = modifyAppointmentTimeMinutesTextField.getText().isEmpty() ? existingAppointmentMinutes : modifyAppointmentTimeMinutesTextField.getText();
        updatedAppointmentYear = modifyAppointmentNewDate.getValue() == null ? existingAppointmentYear : String.valueOf(modifyAppointmentNewDate.getValue().getYear());
        updatedAppointmentMonth = modifyAppointmentNewDate.getValue() == null ? Month.valueOf(existingAppointmentMonth.toUpperCase()) : modifyAppointmentNewDate.getValue().getMonth();
        updatedAppointmentDay = modifyAppointmentNewDate.getValue() == null ? existingAppointmentDay : String.valueOf(modifyAppointmentNewDate.getValue().getDayOfMonth());



        fullAppointmentStartDateTime = LocalDateTime.of(
                Integer.parseInt(updatedAppointmentYear), updatedAppointmentMonth, Integer.parseInt(updatedAppointmentDay), Integer.parseInt(updatedHours), Integer.parseInt(updatedMinutes));
        System.out.println("Date" + fullAppointmentStartDateTime);

        if (modifyAppointmentDurationComboBox.getValue() == null) {
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(existingApptDuration);
        }  else {
            String durationTempStr = modifyAppointmentDurationComboBox.getValue().toString();
            String durationTempArr[]= durationTempStr.split(" ");
            int appointmentDuration = Integer.parseInt(durationTempArr[0]);
            System.out.println("duration " + appointmentDuration);
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(Duration.ofMinutes(appointmentDuration));
        }
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

// TODO figure out fields that are not in the view, like url, contact
//        AlertMessage.display("fields look good", "warning");
        try {
            DBQuery.createQuery("UPDATE appointment SET customerId = " + "'" + updatedCustomerId + "'" + ", title = " + "'" + updatedTitle + "'" + ", description = " + "'" + updatedDescription + "'" +
                    ", location = " + "'" + updatedLocation + "'" + ", start = " + "'" + ConvertTime.convertToUTCTime(fullAppointmentStartDateTime) + "'" + ", end = " + "'" + ConvertTime.convertToUTCTime(fullAppointmentEndDateTime) + "'" +
                    ", createDate = " + "'" + createDate + "'" + ", createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = " + "'" + lastUpdate + "'" + ", lastUpdateBy = " + "'" + loggedInUserName + "'" +
                    " WHERE appointmentId = " + "'" + selectedAppointment.getAppointmentId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (DBQuery.queryNumRowsAffected() > 0) {
            String appointmentId = String.valueOf(selectedAppointment.getAppointmentId());
            Appointment appointmentToUpdate = selectedAppointment;
            appointmentToUpdate.setAppointmentCustomerId(updatedCustomerId);
            appointmentToUpdate.setAppointmentTitle(updatedTitle);
            appointmentToUpdate.setAppointmentDescription(updatedDescription);
            appointmentToUpdate.setAppointmentLocation(updatedLocation);
            appointmentToUpdate.setAppointmentStart(fullAppointmentStartDateTime.format(dtf));
            appointmentToUpdate.setAppointmentEnd(fullAppointmentEndDateTime.format(dtf));
        } else {
            AlertMessage.display("There was an error when updating appointment. Please try again.", "warning");
        }
        loadMainWindowAppointmentModify(event);
    }

    @FXML
    private void loadMainWindowAppointmentModify(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modifyAppointmentDurationComboBox.getItems().addAll("15 mins", "30 mins", "45 mins", "60 mins");

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
