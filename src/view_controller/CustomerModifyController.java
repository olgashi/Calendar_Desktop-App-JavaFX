package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import utilities.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
//TODO check everywhere if any if can be substituted with one liners or ternary
// TODO update all NOW() to proper datetime (format) method
public class CustomerModifyController implements Initializable {
    //TODO change all zip code naming to postalCode
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
    Customer selectedCustomer;
// TODO check Java style guide and make sure everything is formatted accordingly
    public void updateCustomer() throws SQLException {
        int countryId = -1;
        int cityId = -1;
        int addressId = -1;
        String updatedCustomerName = modifyCustomerNameTextField.getText();
        String updatedCustomerAddress = modifyCustomerAddressTextField.getText();
        String updatedCustomerCity = modifyCustomerCityTextField.getText();
        String updatedCustomerCountry = modifyCustomerCountryTextField.getText();
        String updatedCustomerPhone = modifyCustomerPhoneTextField.getText();
        String updatedCustomerZip = modifyCustomerZipCodeTextField.getText();

        String existingCustomerName = selectedCustomer.getCustomerName();
        String existingCustomerAddress = selectedCustomer.getCustomerAddress();
        String existingCustomerCity = selectedCustomer.getCustomerCity();
        String existingCustomerCountry = selectedCustomer.getCustomerCountry();
        String existingCustomerZip = selectedCustomer.getCustomerZipCode();
        String existingCustomerPhone = selectedCustomer.getCustomerPhoneNumber();
        int existingCustomerId = Integer.parseInt(selectedCustomer.getCustomerId());
        // check if all values in input fields remained unchanged
        if (existingCustomerName.equals(updatedCustomerName) && existingCustomerAddress.equals(updatedCustomerAddress) &&
                existingCustomerCity.equals(updatedCustomerCity) && existingCustomerZip.equals(updatedCustomerZip) &&
                existingCustomerCountry.equals(updatedCustomerCountry) && existingCustomerPhone.equals(updatedCustomerPhone)) {
            AlertMessage.display("Customer data did not change. Update values and try again.", "warning");
        } else {
            // country

            if (!existingCustomerCountry.equals(updatedCustomerCountry)) {
                // query database to determine if updated country exists
                dbQuery.createQuery("SELECT country, countryId from country WHERE country = " + "'" + updatedCustomerCountry + "'");
                if (dbQuery.getQueryResultSet().next()) countryId = Integer.parseInt(dbQuery.getQueryResultSet().getString("countryId"));
                else {
//create a new country and get id
                    dbQuery.createQuery("INSERT INTO country SET country = " + "'" + updatedCustomerCountry + "'"
                            + ", createDate = NOW(), createdBy = 'admin', lastUpdate = NOW(), lastUpdateBy = 'admin'");
                    if (dbQuery.queryNumRowsAffected() > 0) countryId = dbQuery.getInsertedRowId();
                }
            } else { //if field wasnt updated go get country id
                dbQuery.createQuery("SELECT country, countryId from country WHERE country = " + "'" + existingCustomerCountry + "'");
                if (dbQuery.getQueryResultSet().next()) countryId = Integer.parseInt(dbQuery.getQueryResultSet().getString("countryId"));
            }

            // city
            if (!existingCustomerCity.equals(updatedCustomerCity)) {
                dbQuery.createQuery("SELECT city, cityId from city WHERE city = " + "'" + updatedCustomerCity + "'");
                if (dbQuery.getQueryResultSet().next()) cityId = Integer.parseInt(dbQuery.getQueryResultSet().getString("cityId"));
                else {
                    dbQuery.createQuery("INSERT INTO city SET city = " + "'" + updatedCustomerCity + "'" +", countryId = " + "'" + countryId + "'"
                            + ", createDate = NOW(), createdBy = 'admin', lastUpdate = NOW(), lastUpdateBy = 'admin'");
                    if (dbQuery.queryNumRowsAffected() > 0) cityId = dbQuery.getInsertedRowId();
                }
            } else {
//                get id for existingcustomercity
                dbQuery.createQuery("SELECT city, cityId from city WHERE city = " + "'" + existingCustomerCity + "'" + "AND countryId = " + "'" + countryId+ "'");
                if (dbQuery.getQueryResultSet().next()) cityId = Integer.parseInt(dbQuery.getQueryResultSet().getString("cityId"));
                else {
                    dbQuery.createQuery("INSERT INTO city SET city = " + "'" + existingCustomerCity + "'" +", countryId = " + "'" + countryId + "'"
                            + ", createDate = NOW(), createdBy = 'admin', lastUpdate = NOW(), lastUpdateBy = 'admin'");
                    if (dbQuery.queryNumRowsAffected() > 0) cityId = dbQuery.getInsertedRowId();
                }
            }

            if (!(existingCustomerZip.equals(updatedCustomerZip) &&
                    existingCustomerAddress.equals(updatedCustomerAddress) &&
                    existingCustomerPhone.equals(updatedCustomerPhone) &&
                    existingCustomerCity.equals(updatedCustomerCity) &&
                    existingCustomerCountry.equals(updatedCustomerCountry))) {
                // insert new address
                dbQuery.createQuery("INSERT INTO address SET address = " + "'" + updatedCustomerAddress +"'" +
                        ", address2 = '', cityId = " + "'" + cityId + "'" + ", postalCode = " + "'" + updatedCustomerZip + "'" +
                        ", phone = " + "'" + updatedCustomerPhone + "'" + ", createDate = NOW(), createdBy = 'admin', lastUpdateBy='admin'");
                if (dbQuery.queryNumRowsAffected() > 0) addressId = dbQuery.getInsertedRowId();

            } else {
                dbQuery.createQuery("SELECT addressId from customer WHERE customerId = " + "'" + existingCustomerId + "'");
                if (dbQuery.getQueryResultSet().next()) addressId = Integer.parseInt(dbQuery.getQueryResultSet().getString("addressId"));
//                get id for existing address
            }


                dbQuery.createQuery("UPDATE customer SET customerName = " + "'" + updatedCustomerName + "'" + ", addressId = " + "'" + addressId + "'" +
                        " WHERE customerId = " + "'" + existingCustomerId + "'");


//                    System.out.println(addressId);


            // address

//        System.out.println(countryId);
        }
    }


    public void initModifyCustomerData(Customer customer) {
        selectedCustomer = customer;
        modifyCustomerNameTextField.setText(selectedCustomer.getCustomerName());
        modifyCustomerAddressTextField.setText(selectedCustomer.getCustomerAddress());
        modifyCustomerCityTextField.setText(selectedCustomer.getCustomerCity());
        modifyCustomerZipCodeTextField.setText(selectedCustomer.getCustomerZipCode());
        modifyCustomerCountryTextField.setText(selectedCustomer.getCustomerCountry());
        modifyCustomerPhoneTextField.setText(selectedCustomer.getCustomerPhoneNumber());
    }
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
