package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import model.Schedule;
import model.User;
import utilities.InputValidation;
import utilities.NewWindow;
import utilities.DBQuery;
import utilities.AlertMessage;
// TODO: research about import statements whats more efficient, and about *
// TODO: add concurrent execution to optimize
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
//TODO:when finished add tests
//TODO later update admin to an actual user creating customer and update time to local time
public class CustomerAddNewController implements Initializable {
    @FXML
    private Text customerAddNewMainWindowLabel, newCustomerNameText, newCustomerAddressText, newCustomerCityText,
            newCustomerZipText, newCustomerCountryText, newCustomerNumberText;
    @FXML
    private TextField newCustomerNameTextField, newCustomerAddressTextField, newCustomerCityTextField,
            newCustomerZipTextField, newCustomerCountryTextField, newCustomerNumberTextField;
    @FXML
    private Button addCustomerCancelButton, addCustomerCreateButton;
    int cityCreatedSuccess, addressCreatedSuccess, countryId, cityId, addressId;
    String loggedInUserName = User.getUserName();


    public void createCustomer(ActionEvent event) throws SQLException, IOException {
        if (!InputValidation.checkForAllEmptyInputs(newCustomerNameTextField, newCustomerAddressTextField, newCustomerCityTextField,
                newCustomerZipTextField, newCustomerCountryTextField, newCustomerNumberTextField)){
            AlertMessage.display("All Fields are required.", "warning");
        } else {
            // check if customer already exists TODO:partial match for address line?
            DBQuery.createQuery("SELECT customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country" +
                    " WHERE customerName = " + "'" + newCustomerNameTextField.getText() + "'" + " AND address = " + "'" + newCustomerAddressTextField.getText() + "'" +
                    " AND city = " + "'" + newCustomerCityTextField.getText() + "'" + " AND postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND country = " +
                    "'" + newCustomerCountryTextField.getText() + "'" + " AND phone = " + "'" + newCustomerNumberTextField.getText() + "'");
            // if customer exists, display a warning to the user
            if (DBQuery.getQueryResultSet().next()) {
                AlertMessage.display("Customer already exists.", "warning");
            } else { //If customer doesn't exist, proceed with creating a new customer, first check if country exists
                DBQuery.createQuery("SELECT country, countryId FROM country WHERE country.country= " + "'" + newCustomerCountryTextField.getText() + "'");
                // if country that user specified doesn't exists, create a new country
                if (!DBQuery.getQueryResultSet().next()) {
//                        TODO: research statement vs prepared statement
                    DBQuery.createQuery("INSERT INTO country (country, createDate, createdBy, lastUpdateBy) values (" + "'" + newCustomerCountryTextField.getText() + "'" + " , NOW()," +
                            "'" + loggedInUserName + "'" + ", " + "'" + loggedInUserName + "'" + ")");
                    if (DBQuery.queryNumRowsAffected() > 0){ // if country was created successfully
                            // get id for that country
                        countryId = DBQuery.getInsertedRowId();
                        System.out.println("Country created, id " + countryId);
                    } else AlertMessage.display("There was an error when creating country " + newCustomerCountryTextField.getText(), "warning");
                   // if country exists get country id
                } else countryId = Integer.parseInt(DBQuery.getQueryResultSet().getString("countryId"));
                System.out.println("Country exists, id " + countryId);
                // check if city exists
                DBQuery.createQuery("SELECT city, cityId FROM city WHERE city.city= " + "'" + newCustomerCityTextField.getText() + "'");
                if (!DBQuery.getQueryResultSet().next()) {// City doesn't exist, create a new city
                    System.out.println("City doesn't exist");
                    DBQuery.createQuery("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values (" +
                                "'" + newCustomerCityTextField.getText() + "'" +","+ "'" + countryId  + "'" + ", NOW(), " +
                            "'" + loggedInUserName + "'" + ", NOW(), " + "'" + loggedInUserName + "'" + ")");
                    if (DBQuery.queryNumRowsAffected() > 0){ // get id for that city
                        System.out.println("City created, rows affected " + cityCreatedSuccess );
                        cityId = DBQuery.getInsertedRowId();
                    } else AlertMessage.display("There was an error when creating city " + newCustomerCityTextField.getText(), "warning");
                } else cityId = Integer.parseInt(DBQuery.getQueryResultSet().getString("cityId"));
                // check if address with given street address (address.address), postalCode, cityId, phone exists
                DBQuery.createQuery("SELECT address, addressId, postalCode, phone FROM address WHERE address = " + "'" + newCustomerAddressTextField.getText() + "'" + " AND" +
                        " postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND"+ " phone = " + "'" + newCustomerNumberTextField.getText() + "'");
                if (!DBQuery.getQueryResultSet().next()) {
                    // if address doesn't exist, create customer with address
//                    TODO update 'test' value for address2
                    DBQuery.createQuery("INSERT INTO address (cityId, address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                                " VALUES (" + "'" + cityId + "'" + ", " + "'" + newCustomerAddressTextField.getText() + "'" + ", 'test', " + "'" + newCustomerZipTextField.getText() + "'" +
                                ", " + "'" + newCustomerNumberTextField.getText() + "'" + ", NOW(), " +"'" + loggedInUserName + "'" +", NOW(), " + "'" + loggedInUserName + "'" + ")");
                    System.out.println("Address was created: " + addressCreatedSuccess);
                    if (DBQuery.queryNumRowsAffected() > 0) {
                        addressId = DBQuery.getInsertedRowId();
                    } else AlertMessage.display("There was an error when creating address ", "warning");
                } else {
                    addressId = Integer.parseInt(DBQuery.getQueryResultSet().getString("addressId"));
                }
            DBQuery.createQuery("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, LastUpdateBy)" +
                " VALUES (" + "'" + newCustomerNameTextField.getText() + "'" + ", " + "'" + addressId +"'" +", " + "1" + ", NOW(), " + "'" + loggedInUserName + "'"
                    + ", NOW(), " +"'" + loggedInUserName + "'" + ")");
            if (DBQuery.queryNumRowsAffected() <= 0) {
                AlertMessage.display("There was an error when creating customer. Please try again.", "warning");
            } else {
                AlertMessage.display("Customer was created successfully!", "warning");
                System.out.println("Customer created by user: " + User.getUserName());
                loadMainWindowCustomerAddNew(event);
            }
            }
            Schedule.addCustomer(new Customer(Schedule.setCustomerId(), newCustomerNameTextField.getText(), newCustomerAddressTextField.getText(), newCustomerCityTextField.getText(),
                    newCustomerZipTextField.getText(), newCustomerCountryTextField.getText(), newCustomerNumberTextField.getText()));
        }
        return;
    }

    public void loadMainWindowCustomerAddNew(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
