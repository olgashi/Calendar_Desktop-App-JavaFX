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


    private boolean validateTime(){
        String regexDigits = "[0-9]+";
        String tHours = newAppointmentTimeHoursTextField.getText();
        String tMinutes = newAppointmentTimeMinutesTextField.getText();

        if ((tHours.length() < 3 && tMinutes.length() < 3) && (tHours.matches(regexDigits) && tMinutes.matches(regexDigits))) return true;
        else return false;
    }

    public boolean validateEmptyInputsAddNewAppointment() {
        String aTitle,aDate, aTimeHours, aTimeMinutes, aLocation, aType, aDescription;
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

        Customer selectedCustomer = appointmentCustomerTable.getSelectionModel().getSelectedItem();
        if (!validateEmptyInputsAddNewAppointment() || !validateTime() || selectedCustomer == null) {
            AlertMessage.display("Inputs are invalid. Please check and try again.", "warning");
        } else {
            // create appointment
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
