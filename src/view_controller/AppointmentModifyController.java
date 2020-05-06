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
import utilities.NewWindow;

import java.io.IOException;
import java.net.URL;
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
    private Text modifyAppointmentCurrentCustomerText;
    @FXML
    private Text modifyAppointmentNewCustomerText;
    @FXML
    private Text modifyAppointmentLocationText;
    @FXML
    private Text modifyAppointmentTypeText;
    @FXML
    private Text modifyAppointmentDescriptionText;
    @FXML
    private Text modifyAppointmentDatePickerCurrentDateText;
    @FXML
    private Text modifyAppointmentDatePickerNewDateText;
    @FXML
    private TextField modifyAppointmentCurrentDate;
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
    private TextField modifyAppointmentCurrentCustomerTextField;
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

    public void initModifyAppointmentData(Appointment appointment, String customerName) {
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
        modifyAppointmentTitleTextField.setText(selectedAppointment.getAppointmentTitle());
        modifyAppointmentCurrentDate.setText(month  + "/" + day + "/" + year);
        modifyAppointmentCurrentDate.setDisable(true);
        modifyAppointmentTimeHoursTextField.setText(hour);
        modifyAppointmentTimeMinutesTextField.setText(minute);
        // preprocess time to fill in time and date
        modifyAppointmentLocationTextField.setText(selectedAppointment.getAppointmentLocation());
        modifyAppointmentTypeTextField.setText(selectedAppointment.getAppointmentType());
        modifyAppointmentDescriptionTextField.setText(selectedAppointment.getAppointmentDescription());
        modifyAppointmentCurrentCustomerTextField.setText(customerName);
        modifyAppointmentCurrentCustomerTextField.setDisable(true);
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
