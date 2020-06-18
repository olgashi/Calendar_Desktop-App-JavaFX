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
import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentModifyController implements Initializable {

    @FXML
    private Text apptModifyMainWindowLabel;
    @FXML
    private Text modifyApptTitleText;
    @FXML
    private Text modifyApptmentDateText;
    @FXML
    private Text modifyApptTimeText;
    @FXML
    private Text modifyApptLocationText;
    @FXML
    private Text modifyApptTypeText;
    @FXML
    private Text modifyApptDescriptionText;
    @FXML
    private Text modifyApptDatePickerCurrentDateText;
    @FXML
    private Text modifyApptContactText;
    @FXML
    private ComboBox<String> modifyApptContactComboBox;
    @FXML
    private Text existingApptTitleText;
    @FXML
    private Text existingApptDateText;
    @FXML
    private Text existingApptTimeText;
    @FXML
    private Text existingApptLocationText;
    @FXML
    private Text existingApptTypeText;
    @FXML
    private Text existingApptDescriptionText;
    @FXML
    private Text existingApptCustomerText;
    @FXML
    private Text existingApptTitleValue;
    @FXML
    private Text existingApptDateValue;
    @FXML
    private Text existingApptTimeValue;
    @FXML
    private Text existingApptLocationValue;
    @FXML
    private Text existingApptTypeValue;
    @FXML
    private Text existingApptDescriptionValue;
    @FXML
    private Text existingApptCustomerValue;
    @FXML
    private Text existingApptDurationText;
    @FXML
    private Text existingApptDurationValue;
    @FXML
    private Text modifyApptDurationText;
    @FXML
    private Text existingApptContactText;
    @FXML
    private Text existingApptContactValue;
    @FXML
    private ComboBox<String> modifyApptDurationComboBox;
    @FXML
    private DatePicker modifyApptNewDate;
    @FXML
    private ComboBox<String> modifyApptTypeComboBox;
    @FXML
    private ComboBox<String> modifyApptHoursComboBox;
    @FXML
    private ComboBox<String> modifyApptMinutesComboBox;
    @FXML
    private TextField modifyApptDescriptionTextField;
    @FXML
    private TextField modifyApptTitleTextField;
    @FXML
    private TextField modifyApptLocationTextField;
    @FXML
    private TableView<Customer> modifyApptCustomerTable;
    @FXML
    private TableColumn<Customer,String> modifyApptCustomerNameColumn;
    @FXML
    private TableColumn<Customer,String> modifyApptCustomerLocationColumn;
    @FXML
    private TableColumn<Customer,String> modifyApptCustomerPhoneNumberColumn;
    @FXML
    private Button modifyApptCancelButton;
    @FXML
    private Button modifyApptCreateButton;
    private Appointment selectedAppt;
    private String existingAppointmentMonth;
    private String existingAppointmentYear;
    private String existingAppointmentDay;
    private String existingAppointmentHours;
    private String existingAppointmentMinutes;
    private String appointmentStartDateTime;
    private String appointmentEndDateTime;
    private String loggedInUserName = User.getUserName();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
//TODO: limit field size for title, location and description in both modify and add
    public void initModifyAppointmentData(Appointment appointment) {
        selectedAppt = appointment;
        appointmentStartDateTime = selectedAppt.getAppointmentStart();
        appointmentEndDateTime = selectedAppt.getAppointmentEnd();
        LocalDateTime apptStart = LocalDateTime.parse(appointmentStartDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s"));
        LocalDateTime apptEnd = LocalDateTime.parse(appointmentEndDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s"));
        Duration duration = Duration.between(apptStart, apptEnd);

        String[] dateTimeArr = appointmentStartDateTime.split(" |T");
        String existingAppointmentStartDate = dateTimeArr[0];
        String existingAppointmentStartTime = dateTimeArr[1];
        // get existing appointment start year, day, month
        String[] yearDayMonthArr = existingAppointmentStartDate.split("-");
        existingAppointmentYear = yearDayMonthArr[0];
        existingAppointmentDay = yearDayMonthArr[2];

        String[] existingApptStartTempArr = existingAppointmentStartDate.split("-");
        int month = Integer.parseInt(existingApptStartTempArr[1]);
        existingAppointmentMonth = new DateFormatSymbols().getMonths()[month-1];
        // get existing appointment start year, day, month
        String[] hourMinArr = existingAppointmentStartTime.split(":");
        existingAppointmentHours = hourMinArr[0];
        existingAppointmentMinutes = hourMinArr[1];

        existingApptTitleValue.setText(selectedAppt.getAppointmentTitle());
        existingApptDateValue.setText(existingAppointmentStartDate);
        existingApptTimeValue.setText(existingAppointmentStartTime);
        existingApptContactValue.setText(selectedAppt.getAppointmentContact());
        existingApptDurationValue.setText(duration.toMinutes() + " mins");
        existingApptLocationValue.setText(selectedAppt.getAppointmentLocation());
        existingApptTypeValue.setText(selectedAppt.getAppointmentType());
        existingApptDescriptionValue.setText(selectedAppt.getAppointmentDescription());
        existingApptCustomerValue.setText(selectedAppt.getAppointmentCustomerName());
    }

    public void updateAppointment(ActionEvent event) throws IOException {
        Customer selectedAppointmentCustomer = modifyApptCustomerTable.getSelectionModel().getSelectedItem();
        LocalDateTime fullAppointmentStartDateTime;
        LocalDateTime fullAppointmentEndDateTime;
        LocalDateTime apptStartDateTimeParsed = LocalDateTime.parse(appointmentStartDateTime, dtf);
        LocalDateTime apptEndDateTimeParsed = LocalDateTime.parse(appointmentEndDateTime, dtf);
        Duration existingApptDuration = Duration.between(apptStartDateTimeParsed, apptEndDateTimeParsed);
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        if (InputValidation.checkForAllEmptyInputs(modifyApptDescriptionTextField, modifyApptTitleTextField,
                modifyApptLocationTextField) && selectedAppointmentCustomer == null && modifyApptNewDate.getValue() == null &&
                modifyApptDurationComboBox.getValue() == null && modifyApptTypeComboBox.getValue() == null &&
                modifyApptContactComboBox.getValue() == null && modifyApptHoursComboBox.getValue() == null &&
                modifyApptMinutesComboBox.getValue() == null) {
            AlertMessage.display("Please provide new values for an appointment and try again.", "warning");
            return;
        }

        String updatedContact = (modifyApptContactComboBox.getValue() == null) ? selectedAppt.getAppointmentContact() : modifyApptContactComboBox.getValue();
        String updatedTitle = modifyApptTitleTextField.getText().isEmpty() ? selectedAppt.getAppointmentTitle() : modifyApptTitleTextField.getText();
        String updatedLocation = modifyApptLocationTextField.getText().isEmpty() ? selectedAppt.getAppointmentLocation() : modifyApptLocationTextField.getText();
        String updatedDescription = modifyApptDescriptionTextField.getText().isEmpty() ? selectedAppt.getAppointmentDescription() : modifyApptDescriptionTextField.getText();
        String updatedCustomerId = selectedAppointmentCustomer == null ? selectedAppt.getAppointmentCustomerId() : selectedAppointmentCustomer.getCustomerId();
        String updatedCustomerName = selectedAppointmentCustomer == null ? selectedAppt.getAppointmentCustomerName() : selectedAppointmentCustomer.getCustomerName();
        String updatedHours = modifyApptHoursComboBox.getValue() == null ? existingAppointmentHours : modifyApptHoursComboBox.getValue();
        String updatedMinutes = modifyApptMinutesComboBox.getValue() == null ? existingAppointmentMinutes : modifyApptMinutesComboBox.getValue();
        String updatedAppointmentYear = modifyApptNewDate.getValue() == null ? existingAppointmentYear : String.valueOf(modifyApptNewDate.getValue().getYear());
        Month updatedAppointmentMonth = modifyApptNewDate.getValue() == null ? Month.valueOf(existingAppointmentMonth.toUpperCase()) : modifyApptNewDate.getValue().getMonth();
        String updatedAppointmentDay = modifyApptNewDate.getValue() == null ? existingAppointmentDay : String.valueOf(modifyApptNewDate.getValue().getDayOfMonth());
        String updatedDuration = modifyApptDurationComboBox.getValue() == null ? String.valueOf(existingApptDuration.toMinutes()) : modifyApptDurationComboBox.getValue().split(" ")[0];
        String updatedType = modifyApptTypeComboBox.getValue() == null ? selectedAppt.getAppointmentType() : modifyApptTypeComboBox.getValue();

        fullAppointmentStartDateTime = LocalDateTime.of(Integer.parseInt(updatedAppointmentYear), updatedAppointmentMonth,
                Integer.parseInt(updatedAppointmentDay), Integer.parseInt(updatedHours), Integer.parseInt(updatedMinutes));
        if (Schedule.overlappingAppointmentsCheck(fullAppointmentStartDateTime, fullAppointmentStartDateTime.plusMinutes(Integer.parseInt(updatedDuration)))) {
            AlertMessage.display("Creating overlapping appointments is not allowed, please select different time and try again", "warning");
            return;
        }
        if (!Schedule.checkIfWithinNormalBusinessHours(fullAppointmentStartDateTime, fullAppointmentStartDateTime.plus(Duration.ofMinutes(Integer.parseInt(updatedDuration))))) {
            AlertMessage.display("The appointment is outside of business hours please correct and try again", "warning");
            return;
        }

        if (modifyApptDurationComboBox.getValue() == null) {
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(existingApptDuration);
        }  else {
            String durationTempStr = modifyApptDurationComboBox.getValue();
            String[] durationTempArr = durationTempStr.split(" ");
            int appointmentDuration = Integer.parseInt(durationTempArr[0]);
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(Duration.ofMinutes(appointmentDuration));
        }

        try {
            DBQuery.createQuery("UPDATE appointment SET customerId = " + "'" + updatedCustomerId + "'" + ", title = " + "'" + updatedTitle + "'" + ", description = " + "'" + updatedDescription + "'" +
                    ", location = " + "'" + updatedLocation + "'" + ", contact = " + "'" + updatedContact + "'" + ", type = " + "'" + updatedType + "'" + ", start = " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentStartDateTime) + "'" + ", end = " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentEndDateTime) + "'" +
                    ", createDate = " + "'" + createDate + "'" + ", createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = " + "'" + lastUpdate + "'" + ", lastUpdateBy = " + "'" + loggedInUserName + "'" +
                    " WHERE appointmentId = " + "'" + selectedAppt.getAppointmentId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (DBQuery.queryNumRowsAffected() > 0) {
            AlertMessage.display("Appointment was updated successfully!", "information");
            Appointment appointmentToUpdate = selectedAppt;
            appointmentToUpdate.setAppointmentCustomerId(updatedCustomerId);
            appointmentToUpdate.setAppointmentCustomerName(updatedCustomerName);
            appointmentToUpdate.setAppointmentTitle(updatedTitle);
            appointmentToUpdate.setAppointmentDescription(updatedDescription);
            appointmentToUpdate.setAppointmentLocation(updatedLocation);
            appointmentToUpdate.setAppointmentType(updatedType);
            appointmentToUpdate.setAppointmentStart(fullAppointmentStartDateTime.format(dtf));
            appointmentToUpdate.setAppointmentEnd(fullAppointmentEndDateTime.format(dtf));
            appointmentToUpdate.setAppointmentContact(updatedContact);
        } else {
            AlertMessage.display("There was an error when updating appointment. Please try again.", "warning");
        }
        loadMainWindowAppointmentModify(event);
    }

    @FXML
    private void loadMainWindowAppointmentModify(ActionEvent event) throws IOException {
        NewWindow.display((Stage) apptModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Callback<DatePicker, DateCell> dayCellFactory = Calendar.customDayCellFactory();
        modifyApptNewDate.setDayCellFactory(dayCellFactory);
        modifyApptDurationComboBox.getItems().addAll("15 mins", "30 mins", "45 mins", "60 mins");
        modifyApptTypeComboBox.getItems().addAll(Reports.allExistingAppointmentTypes());
        modifyApptContactComboBox.getItems().addAll(Reports.allExistingConsultants());
        modifyApptHoursComboBox.getItems().addAll("09","10", "11", "12", "13", "14", "15", "16");
        modifyApptMinutesComboBox.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
        modifyApptCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        modifyApptCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        modifyApptCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        modifyApptCustomerTable.setItems(Customer.getCustomerList());
    }
}
