package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.NewWindow;
import utilities.dbQuery;
import utilities.AlertMessage;
// TODO: research about import statements whats more efficient, and about *
// TODO: add concurrent execution to optimize
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
// REFACTOR!!!!!!!!!
        int cityCreatedSuccess, addressCreatedSuccess, CreatedSuccess, countryCreatedSuccess, countryId, cityId, addressId;
        ResultSet existingCustomer, existingCountry, existingCity, existingAddress, existingPostalCode, existingPhoneNumber;
        ResultSet existingCountryId;
    public void createCustomer(ActionEvent event) throws SQLException, IOException {

        if (!validateEmptyInputsAddNewCustomer()) {
            AlertMessage.display("All Fields are required.");
        } else {
            // check if customer already exists TODO:partial match for address line?
            dbQuery.createQuery("SELECT customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country" +
                    " WHERE customerName = " + "'" + newCustomerNameTextField.getText() + "'" + " AND address = " + "'" + newCustomerAddressTextField.getText() + "'" +
                    " AND city = " + "'" + newCustomerCityTextField.getText() + "'" + " AND postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND country = " +
                    "'" + newCustomerCountryTextField.getText() + "'" + " AND phone = " + "'" + newCustomerNumberTextField.getText() + "'");
            // if customer exists, display a warning to the user
            if (dbQuery.getQueryResultSet().next()) {
                AlertMessage.display("Customer already exists.");
            } else { //If customer doesn't exist, proceed with creating a new customer, first check if country exists
                dbQuery.createQuery("SELECT country, countryId FROM country WHERE country.country= " + "'" + newCustomerCountryTextField.getText() + "'");
                // if country that user specified doesn't exists, create a new country
                if (!dbQuery.getQueryResultSet().next()) {
//                        TODO: research statement vs prepared statement
                    dbQuery.createQuery("INSERT INTO country (country, createDate, createdBy, lastUpdateBy) values (" + "'" + newCustomerCountryTextField.getText() + "'" + " , NOW(), 'admin', 'admin')");
                    if (dbQuery.queryNumRowsAffected() > 0){ // if country was created successfully
                            // get id for that country
                        countryId = dbQuery.getInsertedRowId();
                        System.out.println("Country created, id " + countryId);
                    } else AlertMessage.display("There was an error when creating country " + newCustomerCountryTextField.getText());
                   // if country exists get country id
                } else countryId = Integer.parseInt(existingCountry.getString("countryId"));
                System.out.println("Country exists, id " + countryId);
                // check if city exists
                dbQuery.createQuery("SELECT city, cityId FROM city WHERE city.city= " + "'" + newCustomerCityTextField.getText() + "'");
                if (!dbQuery.getQueryResultSet().next()) {// City doesn't exist, create a new city
                    System.out.println("City doesn't exist");
                    dbQuery.createQuery("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values (" +
                                "'" + newCustomerCityTextField.getText() + "'" +","+ "'" + countryId  + "'" + ", NOW(), 'admin', NOW(), 'admin')");
                    if (dbQuery.queryNumRowsAffected() > 0){ // get id for that city
                        System.out.println("City created, rows affected " + cityCreatedSuccess );
                        cityId = dbQuery.getInsertedRowId();
                    } else AlertMessage.display("There was an error when creating city " + newCustomerCityTextField.getText());
                } else cityId = Integer.parseInt(existingCity.getString("cityId"));
                // check if address with given street address (address.address), postalCode, cityId, phone exists
                dbQuery.createQuery("SELECT address, postalCode, phone FROM address WHERE address = " + "'" + newCustomerAddressTextField.getText() + "'" + " AND" +
                        " postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND"+ " phone = " + "'" + newCustomerNumberTextField.getText() + "'");
                if (!dbQuery.getQueryResultSet().next()) {
                    // if address doesn't exist, create customer with address
//                    TODO update 'test' value for address2
                    dbQuery.createQuery("INSERT INTO address (cityId, address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                                " VALUES (" + "'" + cityId + "'" + ", " + "'" + newCustomerAddressTextField.getText() + "'" + ", 'test', " + "'" + newCustomerZipTextField.getText() + "'" +
                                ", " + "'" + newCustomerNumberTextField.getText() + "'" + ", NOW(), 'admin', NOW(), 'admin')");
                    System.out.println("Address was created: " + addressCreatedSuccess);
                    if (dbQuery.queryNumRowsAffected() > 0) {
                        addressId = dbQuery.getInsertedRowId();
                    } else AlertMessage.display("There was an error when creating address ");
                }
            }
        dbQuery.createQuery("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, LastUpdateBy)" +
                " VALUES (" + "'" + newCustomerNameTextField.getText() + "'" + ", " + "'" + addressId +"'" +", " + "1" + ", NOW(), 'admin', NOW(), 'admin')");
        if (dbQuery.queryNumRowsAffected() <= 0) {
            AlertMessage.display("There was an error when creating customer. Please try again.");
        } else {
            AlertMessage.display("Customer was created successfully!");
            loadMainWindow(event);

        }
        }
        return;
    }

    public boolean validateEmptyInputsAddNewCustomer() {
        String cName,cAddress, cCity, cZip, cCountry, cNumber;
        try {
            cName = newCustomerNameTextField.getText();
            cAddress = newCustomerAddressTextField.getText();
            cCity = newCustomerCityTextField.getText();
            cZip = newCustomerZipTextField.getText();
            cCountry = newCustomerCountryTextField.getText();
            cNumber = newCustomerNumberTextField.getText();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        if (cName.isEmpty() || cAddress.isEmpty() || cCity.isEmpty() || cZip.isEmpty() || cCountry.isEmpty() || cNumber.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
