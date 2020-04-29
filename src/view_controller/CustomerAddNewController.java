package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Customer;
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
//        ResultSet rs;
//        cityId = -99;

        if (!validateEmptyInputsAddNewCustomer()) {
            AlertMessage.display("All Fields are required.");
        } else {
            // check if customer already exists TODO:partial match for address line?
            dbQuery.createQuery("SELECT customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country" +
                    " WHERE customerName = " + "'" + newCustomerNameTextField.getText() + "'" + " AND address = " + "'" + newCustomerAddressTextField.getText() + "'" +
                    " AND city = " + "'" + newCustomerCityTextField.getText() + "'" + " AND postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND country = " +
                    "'" + newCustomerCountryTextField.getText() + "'" + " AND phone = " + "'" + newCustomerNumberTextField.getText() + "'");
            existingCustomer = dbQuery.getQueryResultSet();
            // if customer exists, display a warning to the user
            if (existingCustomer.next()) {
                AlertMessage.display("Customer already exists.");
            } else { //If customer doesn't exist, proceed with creating a new customer
                // first check if country exists
                dbQuery.createQuery("SELECT country, countryId FROM country WHERE country.country= " + "'" + newCustomerCountryTextField.getText() + "'");
                existingCountry = dbQuery.getQueryResultSet();
                // if country that user specified doesn't exists, create a new country
                if (!existingCountry.next()) {
                    // Country doesnt exist, create a new country
//                        TODO: research statement vs prepared statement
                    dbQuery.createQuery("INSERT INTO country (country, createDate, createdBy, lastUpdateBy) values (" + "'" + newCustomerCountryTextField.getText() + "'" + " , NOW(), 'admin', 'admin')");
                    countryCreatedSuccess = dbQuery.queryNumRowsAffected();
                    if (countryCreatedSuccess > 0){ // if country was created successfully
                            // get id for that country
                        dbQuery.getInsertedRowId();
//                            dbQuery.createQuery("SELECT countryId FROM country WHERE country.country= " + "'" + newCustomerCountryTextField.getText() + "'");
//                            countryId = Integer.parseInt(dbQuery.getQueryResultSet().getString("countryId"));
                    } else AlertMessage.display("There was an error when creating country " + newCustomerCountryTextField.getText());
                   // if country exists grab country id and typecast to int
                } else countryId = Integer.parseInt(existingCountry.getString("countryId"));
                System.out.println("Country exists, id " + countryId);
                // check if city exists
                dbQuery.createQuery("SELECT city, cityId FROM city WHERE city.city= " + "'" + newCustomerCityTextField.getText() + "'");
                existingCity = dbQuery.getQueryResultSet();
                if (!existingCity.next()) {// City doesn't exist, create a new city
                    System.out.println("City doesn't exist");
                    dbQuery.createQuery("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values (" +
                                "'" + newCustomerCityTextField.getText() + "'" +","+ "'" + countryId  + "'" + ", NOW(), 'admin', NOW(), 'admin')");
                    cityCreatedSuccess = dbQuery.queryNumRowsAffected();
                    if (cityCreatedSuccess > 0){ // get id for that city
                        cityId = dbQuery.getInsertedRowId();
//                            dbQuery.createQuery("SELECT cityId FROM city WHERE city.city= " + "'" + newCustomerCityTextField.getText() + "'");
//                            cityId = Integer.parseInt(dbQuery.getQueryResultSet().getString("cityId"));
                    } else AlertMessage.display("There was an error when creating city " + newCustomerCityTextField.getText());
                } else cityId = Integer.parseInt(existingCity.getString("cityId"));
                // check if address with given street address (address.address), postalCode, cityId, phone exists
                dbQuery.createQuery("SELECT address, postalCode, phone FROM address WHERE address = " + "'" + newCustomerAddressTextField.getText() + "'" + " AND" +
                        " postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND"+ " phone = " + "'" + newCustomerNumberTextField.getText() + "'");
                existingAddress = dbQuery.getQueryResultSet();
                if (!existingAddress.next()) {
                    // if address doesn't exist, create customer with address
//                    TODO update 'test' value for address2
                    dbQuery.createQuery("INSERT INTO address (cityId, address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                                " VALUES (" + "'" + cityId + "'" + ", " + "'" + newCustomerAddressTextField.getText() + "'" + ", 'test', " + "'" + newCustomerZipTextField.getText() + "'" +
                                ", " + "'" + newCustomerNumberTextField.getText() + "'" + ", NOW(), 'admin', NOW(), 'admin')");
                    addressCreatedSuccess = dbQuery.queryNumRowsAffected();
                    System.out.println("Address was created: " + addressCreatedSuccess);
                    if (addressCreatedSuccess > 0) {
                        addressId = dbQuery.getInsertedRowId();
                    } else AlertMessage.display("There was an error when creating address ");

                }
            }
        dbQuery.createQuery("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, LastUpdateBy)" +
                " VALUES (" + "'" + newCustomerNameTextField.getText() + "'" + ", " + "'" + addressId +"'" +", " + "1" + ", NOW(), 'admin', NOW(), 'admin')");
        if (dbQuery.queryNumRowsAffected() <= 0) {
            AlertMessage.display("There was an error when creating customer. Please try again.");
        } else {
            closeCustomerAddNew(event);

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

    // close window
    public void closeCustomerAddNew(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
