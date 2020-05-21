package controller;

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
// TODO: add concurrent execution to optimize
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CustomerAddNewController implements Initializable {
    @FXML
    private Text customerAddNewMainWindowLabel, newCustomerNameText, newCustomerAddressText, newCustomerCityText,
            newCustomerZipText, newCustomerCountryText, newCustomerNumberText;
    @FXML
    private TextField newCustomerNameTextField, newCustomerAddressTextField, newCustomerCityTextField,
            newCustomerZipTextField, newCustomerCountryTextField, newCustomerNumberTextField;
    @FXML
    private Button addCustomerCancelButton, addCustomerCreateButton;
    int countryId, cityId, addressId;
    String address2;
    LocalDateTime createDate, lastUpdate;
    String loggedInUserName = User.getUserName();

    public void createCustomer(ActionEvent event) throws SQLException, IOException {
        address2 = "not provided";
        createDate = lastUpdate = LocalDateTime.now();

        if (InputValidation.checkForAnyEmptyInputs(newCustomerNameTextField, newCustomerAddressTextField, newCustomerCityTextField,
                newCustomerZipTextField, newCustomerCountryTextField, newCustomerNumberTextField)){

            AlertMessage.display("All Fields are required.", "warning");

        } else {

            checkIfCustomerExistsDB();
            if (DBQuery.getQueryResultSet().next()) AlertMessage.display("Customer already exists.", "warning");
            else {

                DBQuery.createQuery("SELECT country, countryId FROM country WHERE country.country= " + "'" + newCustomerCountryTextField.getText() + "'");
                if (!DBQuery.getQueryResultSet().next()) {

                    createNewCountryDB();
                    if (DBQuery.queryNumRowsAffected() > 0)countryId = DBQuery.getInsertedRowId();
                    else AlertMessage.display("There was an error when creating country " + newCustomerCountryTextField.getText(), "warning");

                } else countryId = Integer.parseInt(DBQuery.getQueryResultSet().getString("countryId"));

                DBQuery.createQuery("SELECT city, cityId FROM city WHERE city.city= " + "'" + newCustomerCityTextField.getText() + "'");

                if (!DBQuery.getQueryResultSet().next()) {
                    createCityDB();
                    if (DBQuery.queryNumRowsAffected() > 0) cityId = DBQuery.getInsertedRowId();
                    else AlertMessage.display("There was an error when creating city " + newCustomerCityTextField.getText(), "warning");
                } else cityId = Integer.parseInt(DBQuery.getQueryResultSet().getString("cityId"));

                DBQuery.createQuery("SELECT address, addressId, postalCode, phone FROM address WHERE address = " + "'" + newCustomerAddressTextField.getText() + "'" + " AND" +
                        " postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND"+ " phone = " + "'" + newCustomerNumberTextField.getText() + "'");

                if (!DBQuery.getQueryResultSet().next()) {

                    createCustomerAddressDB();
                    if (DBQuery.queryNumRowsAffected() > 0) addressId = DBQuery.getInsertedRowId();
                    else AlertMessage.display("There was an error when creating address ", "warning");

                } else addressId = Integer.parseInt(DBQuery.getQueryResultSet().getString("addressId"));

                createCustomerDB();

                if (DBQuery.queryNumRowsAffected() > 0) {
                    addCustomerToSchedule();
                    AlertMessage.display("Customer was created successfully!", "information");
                    loadMainWindowCustomerAddNew(event);
            } else AlertMessage.display("There was an error when creating customer. Please try again.", "warning");
            }
        }
        return;
    }

    public void addCustomerToSchedule() {
        Schedule.addCustomer(new Customer(Schedule.setCustomerId(), newCustomerNameTextField.getText(), newCustomerAddressTextField.getText(), newCustomerCityTextField.getText(),
                newCustomerZipTextField.getText(), newCustomerCountryTextField.getText(), newCustomerNumberTextField.getText()));
    }

    public void createCityDB() throws SQLException {
        DBQuery.createQuery("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values (" +
                    "'" + newCustomerCityTextField.getText() + "'" +","+ "'" + countryId  + "'" + ", " + "'" + createDate + "'" + ", " +
                "'" + loggedInUserName + "'" + ", " + "'" + lastUpdate + "'" + ", " + "'" + loggedInUserName + "'" + ")");
    }

    public void createCustomerAddressDB() throws SQLException {
        DBQuery.createQuery("INSERT INTO address (cityId, address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                    " VALUES (" + "'" + cityId + "'" + ", " + "'" + newCustomerAddressTextField.getText() + "'" + ", " + "'" + address2 + "'" + ", " + "'" + newCustomerZipTextField.getText() + "'" +
                    ", " + "'" + newCustomerNumberTextField.getText() + "'" + ", " + "'" + createDate + "'" + ", " + "'" + loggedInUserName + "'" + ", " + "'" + lastUpdate + "'" + ", " + "'" + loggedInUserName + "'" + ")");
    }

    public void createCustomerDB() throws SQLException {
        DBQuery.createQuery("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, LastUpdateBy)" +
            " VALUES (" + "'" + newCustomerNameTextField.getText() + "'" + ", " + "'" + addressId +"'" +", " + "1," + "'" + createDate + "'" + ", " + "'" + loggedInUserName + "'"
                + ", " + "'" + lastUpdate + "'" + ", " + "'" + loggedInUserName + "'" + ")");
    }

    public void createNewCountryDB() throws SQLException {
        DBQuery.createQuery("INSERT INTO country (country, createDate, createdBy, lastUpdateBy) values (" + "'" + newCustomerCountryTextField.getText() + "'" + ", " + "'" + createDate + "'" + ", " +
                "'" + loggedInUserName + "'" + ", " + "'" + loggedInUserName + "'" + ")");
    }

    public void checkIfCustomerExistsDB() throws SQLException {
        DBQuery.createQuery("SELECT customerName, address, city, postalCode, country, phone FROM customer, address, city, country" +
                " WHERE customerName = " + "'" + newCustomerNameTextField.getText() + "'" + " AND address = " + "'" + newCustomerAddressTextField.getText() + "'" +
                " AND city = " + "'" + newCustomerCityTextField.getText() + "'" + " AND postalCode = " + "'" + newCustomerZipTextField.getText() + "'" + " AND country = " +
                "'" + newCustomerCountryTextField.getText() + "'" + " AND phone = " + "'" + newCustomerNumberTextField.getText() + "'");
    }

    public void loadMainWindowCustomerAddNew(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerAddNewMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
