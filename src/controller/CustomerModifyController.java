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
import utilities.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerModifyController implements Initializable {
    @FXML
    private Text customerModifyMainWindowLabel;
    @FXML
    private Text modifyCustomerNameText;
    @FXML
    private Text modifyCustomerAddressText;
    @FXML
    private Text modifyCustomerCityText;
    @FXML
    private Text modifyCustomerZipCodeText;
    @FXML
    private Text modifyCustomerCountryText;
    @FXML
    private Text modifyCustomerPhoneText;
    @FXML
    private Text existingCustomerNameText;
    @FXML
    private Text existingCustomerNameValue;
    @FXML
    private Text existingCustomerAddressText;
    @FXML
    private Text existingCustomerAddressValue;
    @FXML
    private Text existingCustomerCityText;
    @FXML
    private Text existingCustomerCityValue;
    @FXML
    private Text existingCustomerZipCodeText;
    @FXML
    private Text existingCustomerZipCodeValue;
    @FXML
    private Text existingCustomerCountryText;
    @FXML
    private Text existingCustomerCountryValue;
    @FXML
    private Text existingCustomerPhoneText;
    @FXML
    private Text existingCustomerPhoneValue;
    @FXML
    private TextField modifyCustomerNameTextField;
    @FXML
    private TextField modifyCustomerAddressTextField;
    @FXML
    private TextField modifyCustomerCityTextField;
    @FXML
    private TextField modifyCustomerZipCodeTextField;
    @FXML
    private TextField modifyCustomerCountryTextField;
    @FXML
    private TextField modifyCustomerPhoneTextField;
    @FXML
    private Button modifyCustomerCancelButton;
    @FXML
    private Button modifyCustomerUpdateButton;
    private Customer selectedCustomer;

    public void updateCustomer(ActionEvent event) throws SQLException, IOException {
        int countryId = -1;
        int cityId = -1;
        int addressId = -1;

        String existingCustomerName = selectedCustomer.getCustomerName();
        String existingCustomerAddress = selectedCustomer.getCustomerAddress();
        String existingCustomerCity = selectedCustomer.getCustomerCity();
        String existingCustomerCountry = selectedCustomer.getCustomerCountry();
        String existingCustomerZip = selectedCustomer.getCustomerZipCode();
        String existingCustomerPhone = selectedCustomer.getCustomerPhoneNumber();
        int existingCustomerId = Integer.parseInt(selectedCustomer.getCustomerId());
        String loggedInUserName = User.getUserName();

        if (InputValidation.checkForAllEmptyInputs(modifyCustomerNameTextField, modifyCustomerAddressTextField, modifyCustomerCityTextField,
                modifyCustomerZipCodeTextField, modifyCustomerCountryTextField, modifyCustomerPhoneTextField)) {
            AlertMessage.display("Please provide new values and try again.", "warning");
            return;
        } else {
            String updatedCustomerName = modifyCustomerNameTextField.getText().isEmpty() ? existingCustomerName : modifyCustomerNameTextField.getText();
            String updatedCustomerAddress = modifyCustomerAddressTextField.getText().isEmpty() ? existingCustomerAddress : modifyCustomerAddressTextField.getText();
            String updatedCustomerCity = modifyCustomerCityTextField.getText().isEmpty() ? existingCustomerCity : modifyCustomerCityTextField.getText();
            String updatedCustomerCountry = modifyCustomerCountryTextField.getText().isEmpty() ? existingCustomerCountry : modifyCustomerCountryTextField.getText();
            String updatedCustomerPhone = modifyCustomerPhoneTextField.getText().isEmpty() ? existingCustomerPhone : modifyCustomerPhoneTextField.getText();
            String updatedCustomerZip = modifyCustomerZipCodeTextField.getText().isEmpty() ? existingCustomerZip : modifyCustomerZipCodeTextField.getText();

            if (!existingCustomerCountry.equals(updatedCustomerCountry)) {

                DBQuery.createQuery("SELECT country, countryId from country WHERE country = " + "'" + updatedCustomerCountry + "'");
                if (DBQuery.getQueryResultSet().next()) countryId = Integer.parseInt(DBQuery.getQueryResultSet().getString("countryId"));
                else {

                    createCountryDB(updatedCustomerCountry, loggedInUserName);
                    if (DBQuery.queryNumRowsAffected() > 0) countryId = DBQuery.getInsertedRowId();

                }
            } else {

                DBQuery.createQuery("SELECT country, countryId from country WHERE country = " + "'" + existingCustomerCountry + "'");
                if (DBQuery.getQueryResultSet().next()) countryId = Integer.parseInt(DBQuery.getQueryResultSet().getString("countryId"));

            }

            if (!existingCustomerCity.equals(updatedCustomerCity)) {

                DBQuery.createQuery("SELECT city, cityId from city WHERE city = " + "'" + updatedCustomerCity + "'");
                if (DBQuery.getQueryResultSet().next()) cityId = Integer.parseInt(DBQuery.getQueryResultSet().getString("cityId"));
                else {

                    DBQuery.createQuery("INSERT INTO city SET city = " + "'" + updatedCustomerCity + "'" +", countryId = " + "'" + countryId + "'"
                            + ", createDate = NOW(), createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = NOW(), lastUpdateBy = " + "'" + loggedInUserName + "'");
                    if (DBQuery.queryNumRowsAffected() > 0) cityId = DBQuery.getInsertedRowId();
                }
            } else {

                DBQuery.createQuery("SELECT city, cityId from city WHERE city = " + "'" + existingCustomerCity + "'" + "AND countryId = " + "'" + countryId+ "'");
                if (DBQuery.getQueryResultSet().next()) cityId = Integer.parseInt(DBQuery.getQueryResultSet().getString("cityId"));
                else {
                    DBQuery.createQuery("INSERT INTO city SET city = " + "'" + existingCustomerCity + "'" +", countryId = " + "'" + countryId + "'"
                            + ", createDate = NOW(), createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = NOW(), lastUpdateBy = " + "'" + loggedInUserName + "'");
                    if (DBQuery.queryNumRowsAffected() > 0) cityId = DBQuery.getInsertedRowId();
                }
            }

            if (!(existingCustomerZip.equals(updatedCustomerZip) && existingCustomerAddress.equals(updatedCustomerAddress) &&
                    existingCustomerPhone.equals(updatedCustomerPhone) && existingCustomerCity.equals(updatedCustomerCity) &&
                    existingCustomerCountry.equals(updatedCustomerCountry))) {

                createCustomerAddressDB(cityId, updatedCustomerAddress, updatedCustomerPhone, updatedCustomerZip, loggedInUserName);
                if (DBQuery.queryNumRowsAffected() > 0) addressId = DBQuery.getInsertedRowId();

            } else {

                DBQuery.createQuery("SELECT addressId from customer WHERE customerId = " + "'" + existingCustomerId + "'");
                if (DBQuery.getQueryResultSet().next()) addressId = Integer.parseInt(DBQuery.getQueryResultSet().getString("addressId"));

            }
            updateCustomerDB(addressId, updatedCustomerName, existingCustomerId);

            if (DBQuery.queryNumRowsAffected() > 0) {
                AlertMessage.display("Customer was updated successfully!", "information");
                String customerId = String.valueOf(existingCustomerId);
                Customer customerToUpdate = Schedule.lookupCustomerById(customerId);
                customerToUpdate.setCustomerName(updatedCustomerName);
                customerToUpdate.setCustomerAddress(updatedCustomerAddress);
                customerToUpdate.setCustomerCity(updatedCustomerCity);
                customerToUpdate.setCustomerZipCode(updatedCustomerZip);
                customerToUpdate.setCustomerCountry(updatedCustomerCountry);
                customerToUpdate.setCustomerPhoneNumber(updatedCustomerPhone);
            } else {
                AlertMessage.display("There was an error when updating customer. Please try again.", "warning");
            }
        }
        loadMainWindow(event);
    }

    private void updateCustomerDB(int addressId, String updatedCustomerName, int existingCustomerId) throws SQLException {
        DBQuery.createQuery("UPDATE customer SET customerName = " + "'" + updatedCustomerName + "'" + ", addressId = " + "'" + addressId + "'" +
                    " WHERE customerId = " + "'" + existingCustomerId + "'");
    }

    private void createCustomerAddressDB(int cityId, String updatedCustomerAddress, String updatedCustomerPhone, String updatedCustomerZip, String loggedInUserName) throws SQLException {
        DBQuery.createQuery("INSERT INTO address SET address = " + "'" + updatedCustomerAddress +"'" +
                ", address2 = '', cityId = " + "'" + cityId + "'" + ", postalCode = " + "'" + updatedCustomerZip + "'" +
                ", phone = " + "'" + updatedCustomerPhone + "'" + ", createDate = NOW(), createdBy = " +"'"+ loggedInUserName +
                "'" +", lastUpdateBy = " + "'" + loggedInUserName + "'");
    }

    private void createCountryDB(String updatedCustomerCountry, String loggedInUserName) throws SQLException {
        DBQuery.createQuery("INSERT INTO country SET country = " + "'" + updatedCustomerCountry + "'"
                + ", createDate = NOW(), createdBy = " + "'" + loggedInUserName + "'" + ", lastUpdate = NOW(), lastUpdateBy = " + "'" + loggedInUserName + "'");
    }

    void initModifyCustomerData(Customer customer) {
        selectedCustomer = customer;
        existingCustomerNameValue.setText(selectedCustomer.getCustomerName());
        existingCustomerAddressValue.setText(selectedCustomer.getCustomerAddress());
        existingCustomerCityValue.setText(selectedCustomer.getCustomerCity());
        existingCustomerZipCodeValue.setText(selectedCustomer.getCustomerZipCode());
        existingCustomerCountryValue.setText(selectedCustomer.getCustomerCountry());
        existingCustomerPhoneValue.setText(selectedCustomer.getCustomerPhoneNumber());
    }
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
