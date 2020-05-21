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
    private Text modifyAppointmentContactText;
    @FXML
    private ComboBox modifyAppointmentContactComboBox;
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
    private Text existingAppointmentContactText;
    @FXML
    private Text existingAppointmentContactValue;
    @FXML
    private ComboBox modifyAppointmentDurationComboBox;
    @FXML
    private DatePicker modifyAppointmentNewDate;
    @FXML
    private ComboBox modifyAppointmentTypeComboBox;
    @FXML
    private ComboBox modifyAppointmentHoursComboBox;
    @FXML
    private ComboBox modifyAppointmentMinutesComboBox;
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
    private String updatedCustomerId;
    private String updatedTitle;
    private String updatedHours;
    private String updatedMinutes;
    private String updatedAppointmentYear;
    private String updatedAppointmentDay;
    private String updatedLocation;
    private String updatedType;
    private String updatedDescription;
    private String updatedContact;
    private String updatedDuration;
    private Month updatedAppointmentMonth;
    private String existingAppointmentMonth;
    private String existingAppointmentYear, existingAppointmentDay, existingAppointmentHours,
            existingAppointmentMinutes, existingAppointmentStartDate, existingAppointmentStartTime, appointmentStartDateTime;
    String appointmentEndDateTime;
    String loggedInUserName = User.getUserName();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public void initModifyAppointmentData(Appointment appointment) {
        selectedAppointment = appointment;
        appointmentStartDateTime = selectedAppointment.getAppointmentStart();
        appointmentEndDateTime = selectedAppointment.getAppointmentEnd();
        LocalDateTime apptStart = LocalDateTime.parse(appointmentStartDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s"));
        LocalDateTime apptEnd = LocalDateTime.parse(appointmentEndDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s"));
        Duration duration = Duration.between(apptStart, apptEnd);

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
        existingAppointmentContactValue.setText(selectedAppointment.getAppointmentContact());
        existingAppointmentDurationValue.setText(String.valueOf(duration.toMinutes()) + " mins");
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
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        if (!InputValidation.checkForAnyEmptyInputs(modifyAppointmentDescriptionTextField, modifyAppointmentTitleTextField,
                modifyAppointmentLocationTextField) && selectedAppointmentCustomer == null && modifyAppointmentNewDate.getValue() == null &&
                modifyAppointmentDurationComboBox.getValue() == null && modifyAppointmentTypeComboBox.getValue() == null &&
                modifyAppointmentContactComboBox.getValue() == null) {
            AlertMessage.display("Please provide new values for an appointment and try again.", "warning");
            return;
        }

        updatedContact = (modifyAppointmentContactComboBox.getValue() == null) ?  selectedAppointment.getAppointmentContact() : modifyAppointmentContactComboBox.getValue().toString();

        updatedTitle = modifyAppointmentTitleTextField.getText().isEmpty() ? selectedAppointment.getAppointmentTitle() : modifyAppointmentTitleTextField.getText();
        updatedLocation = modifyAppointmentLocationTextField.getText().isEmpty() ? selectedAppointment.getAppointmentLocation() : modifyAppointmentLocationTextField.getText();
        updatedType = modifyAppointmentTypeComboBox.getValue() == null ? selectedAppointment.getAppointmentType() : modifyAppointmentLocationTextField.getText();
        updatedDescription = modifyAppointmentDescriptionTextField.getText().isEmpty() ? selectedAppointment.getAppointmentDescription() : modifyAppointmentDescriptionTextField.getText();
        updatedCustomerId = selectedAppointmentCustomer == null ? selectedAppointment.getAppointmentCustomerId() : selectedAppointmentCustomer.getCustomerId();

        updatedHours = modifyAppointmentHoursComboBox.getValue() == null ? existingAppointmentHours : modifyAppointmentHoursComboBox.getValue().toString();
        updatedMinutes = modifyAppointmentMinutesComboBox.getValue() == null ? existingAppointmentMinutes : modifyAppointmentMinutesComboBox.getValue().toString();

        updatedAppointmentYear = modifyAppointmentNewDate.getValue() == null ? existingAppointmentYear : String.valueOf(modifyAppointmentNewDate.getValue().getYear());
        updatedAppointmentMonth = modifyAppointmentNewDate.getValue() == null ? Month.valueOf(existingAppointmentMonth.toUpperCase()) : modifyAppointmentNewDate.getValue().getMonth();
        updatedAppointmentDay = modifyAppointmentNewDate.getValue() == null ? existingAppointmentDay : String.valueOf(modifyAppointmentNewDate.getValue().getDayOfMonth());
        updatedDuration = modifyAppointmentDurationComboBox.getValue() == null ? String.valueOf(existingApptDuration.toMinutes()) : modifyAppointmentDurationComboBox.getValue().toString().split(" ")[0];
        updatedType = modifyAppointmentTypeComboBox.getValue() == null ? selectedAppointment.getAppointmentType() : modifyAppointmentTypeComboBox.getValue().toString();

        fullAppointmentStartDateTime = LocalDateTime.of(Integer.parseInt(updatedAppointmentYear), updatedAppointmentMonth,
                Integer.parseInt(updatedAppointmentDay), Integer.parseInt(updatedHours), Integer.parseInt(updatedMinutes));
        if (Schedule.overlappingAppointmentsCheck(fullAppointmentStartDateTime, Integer.parseInt(updatedCustomerId), Integer.parseInt(selectedAppointment.getAppointmentId()))) {
            AlertMessage.display("Creating overlapping appointments is not allowed, please select different time and try again", "warning");
            return;
        }

        if (modifyAppointmentDurationComboBox.getValue() == null) {
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(existingApptDuration);
        }  else {
            String durationTempStr = modifyAppointmentDurationComboBox.getValue().toString();
            String durationTempArr[]= durationTempStr.split(" ");
            int appointmentDuration = Integer.parseInt(durationTempArr[0]);
            fullAppointmentEndDateTime = fullAppointmentStartDateTime.plus(Duration.ofMinutes(appointmentDuration));
        }

// TODO figure out fields that are not in the view, like url, contact
        try {
            DBQuery.createQuery("UPDATE appointment SET customerId = " + "'" + updatedCustomerId + "'" + ", title = " + "'" + updatedTitle + "'" + ", description = " + "'" + updatedDescription + "'" +
                    ", location = " + "'" + updatedLocation + "'" + ", contact = " + "'" + updatedContact + "'" + ", type = " + "'" + updatedType + "'" + ", start = " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentStartDateTime) + "'" + ", end = " + "'" + DateTimeUtils.convertToUTCTime(fullAppointmentEndDateTime) + "'" +
                    ", createDate = " + "'" + createDate + "'" + ", createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = " + "'" + lastUpdate + "'" + ", lastUpdateBy = " + "'" + loggedInUserName + "'" +
                    " WHERE appointmentId = " + "'" + selectedAppointment.getAppointmentId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (DBQuery.queryNumRowsAffected() > 0) {
            Appointment appointmentToUpdate = selectedAppointment;
            appointmentToUpdate.setAppointmentCustomerId(updatedCustomerId);
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
        NewWindow.display((Stage) appointmentModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentsMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Callback<DatePicker, DateCell> dayCellFactory = Calendar.customDayCellFactory();
        modifyAppointmentNewDate.setDayCellFactory(dayCellFactory);
        modifyAppointmentDurationComboBox.getItems().addAll("15 mins", "30 mins", "45 mins", "60 mins");
        modifyAppointmentTypeComboBox.getItems().addAll(Reports.allExistingAppointmentTypes());
        modifyAppointmentContactComboBox.getItems().addAll(Reports.allExistingConsultants());
        modifyAppointmentHoursComboBox.getItems().addAll("09","10", "11", "12", "13", "14", "15", "16", "17");
        modifyAppointmentMinutesComboBox.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
        //        TODO extract this to a method?
        modifyAppointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        modifyAppointmentCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        modifyAppointmentCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        modifyAppointmentCustomerTable.setItems(Customer.getCustomerList());
    }
}
