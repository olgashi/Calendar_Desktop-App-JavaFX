package view_controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Customer;
import utilities.AlertMessage;
import utilities.dbQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private DatePicker newAppointmentDatePicker;
    @FXML
    private RadioButton timeAM;
    @FXML
    private RadioButton timePM;
    @FXML
    private ToggleGroup amPMtoggleGroup;
    @FXML
    private TextField newAppointmentTimeHoursTextField;
    @FXML
    private TextField newAppointmentTimeMinutesTextField;
    @FXML
    private TextField newAppointmentTypeTextField;
    @FXML
    private TextField newAppointmentDescriptionTextField;
    @FXML
    private TextField newAppointmentTitleTextField;
    @FXML
    private TextField newAppointmentLocationTextField;
    @FXML
    private TableView<Customer> appointmentCustomerTable;
    @FXML
    private TableColumn<Customer,String> appointmentCustomerNameColumn;
    @FXML
    private TableColumn<Customer,String> appointmentCustomerLocationColumn;
    @FXML
    private TableColumn<Customer,String> appointmentCustomerPhoneNumberColumn;
    @FXML
    private Button addAppointmentCancelButton;
    @FXML
    private Button addAppointmentCreateButton;
    private Customer selectedCustomer;
    private String selectedCustomerId;
    private String aTitle,aDate, aTimeHours, aTimeMinutes, aLocation, aType, aDescription;
//            TODO add check for conflicting appointment
//            TODO add end time
//            TODO add "information" to alertmessage.display
//    TODO user should not be able to add appointments outside business hours and on the weekends
//    TODO user

    private boolean validateTime(){
        String tHours;
        String tMinutes;
        String regexDigits = "[0-9]+";
        try {
            tHours = newAppointmentTimeHoursTextField.getText();
            tMinutes = newAppointmentTimeMinutesTextField.getText();
        } catch (NumberFormatException e) {
            AlertMessage.display("Please enter numbers only in hour and minute fields.", "warning");
            e.printStackTrace();
            return false;
        }


        if ((tHours.length() < 3 && tMinutes.length() < 3) && (Pattern.matches(regexDigits, tHours) && Pattern.matches(regexDigits,tMinutes))) return true;
        else return false;
    }

    public boolean validateEmptyInputsAddNewAppointment() {
        try {
            aTitle = newAppointmentTitleTextField.getText();
            aDate = newAppointmentDateText.getText();
            aTimeHours = newAppointmentTimeHoursTextField.getText();
            aTimeMinutes = newAppointmentTimeMinutesTextField.getText();
            aLocation = newAppointmentLocationTextField.getText();
            aType = newAppointmentTypeTextField.getText();
            aDescription = newAppointmentDescriptionTextField.getText();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        if (aTitle.isEmpty() || aDate.isEmpty() || aTimeHours.isEmpty() || aTimeMinutes.isEmpty() ||
                aLocation.isEmpty() || aType.isEmpty() || aDescription.isEmpty()) return false;
        else return true;
    }

    private void loadCustomerTableData (){
        Customer.clearCustomerList();
        dbQuery.createQuery("SELECT customerId, customerName, address, city, postalCode, country, phone FROM customer, address, city, country " +
                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
        ResultSet rs = dbQuery.getQueryResultSet();
        try {
            while(dbQuery.getQueryResultSet().next()) {
                Customer.getCustomerList().add(new Customer(rs.getString("customerId"), rs.getString("customerName"),
                        rs.getString("address"), rs.getString("city"), rs.getString("postalCode"),
                        rs.getString("country"), rs.getString("phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAppointment(ActionEvent event) throws SQLException, IOException {
        LocalDateTime fullAppointmentDateTime;
        selectedCustomer = appointmentCustomerTable.getSelectionModel().getSelectedItem();
//        LocalDate date = newAppointmentDatePicker.getValue();
//        LocalDateTime myDateObj = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//        String dt = date.toString() + aTimeHours + ':' + aTimeMinutes;
//        String formattedDate = dt.format(formatter);
        if (!validateEmptyInputsAddNewAppointment()) {
            AlertMessage.display("All fields are required. Please make try again.", "warning");
            return;
        }
        if (!validateTime()) {
            AlertMessage.display("Time has to be numbers only. Please make try again.", "warning");
            return;
        }
        if (selectedCustomer == null) {
            AlertMessage.display("Please select a customer for this appointment.", "warning");
            return;
        } else {

                    fullAppointmentDateTime = LocalDateTime.of(
                    newAppointmentDatePicker.getValue().getYear(),
                    newAppointmentDatePicker.getValue().getMonthValue(),
                    newAppointmentDatePicker.getValue().getDayOfMonth(),
                    Integer.parseInt(newAppointmentTimeHoursTextField.getText()),
                    Integer.parseInt(newAppointmentTimeMinutesTextField.getText()));

//            LocalDateTime endAppt = apptDateTime.plusMinutes(Long.parseLong(duration.getValue()));
//            AlertMessage.display(formattedDate, "warning");

            // create appointment
//             String aTitle,aDate, aTimeHours, aTimeMinutes, aLocation, aType, aDescription;
//            selectedCustomerId = selectedCustomer.getCustomerId();
//            dbQuery.createQuery("INSERT INTO appointment (customerId, userId, title, description, location, type, start) values()");
        System.out.println(fullAppointmentDateTime);

        }
    }

//Table is not populating
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amPMtoggleGroup = new ToggleGroup();
        this.timeAM.setToggleGroup(amPMtoggleGroup);
        this.timePM.setToggleGroup(amPMtoggleGroup);
        timeAM.setSelected(true);

        loadCustomerTableData();
        appointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerName"));
        appointmentCustomerLocationColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerCity"));
        appointmentCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerPhoneNumber"));
        appointmentCustomerTable.setItems(Customer.getCustomerList());
    }
}
// TODO include info popups on hover for input fields
