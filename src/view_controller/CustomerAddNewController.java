package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import utilities.DBConnection;
import utilities.dbQuery;
import utilities.AlertMessage;
// TODO: research about import statements whats more efficient, and about *
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
//TODO:when finished add tests
public class CustomerAddNewController implements Initializable {
    @FXML
    private Text customerAddNewMainWindowLabel;
    @FXML
    private Text newCustomerNameText;
    @FXML
    private Text newCustomerAddressText;
    @FXML
    private Text newCustomerCityText;
    @FXML
    private Text newCustomerZipText;
    @FXML
    private Text newCustomerCountryText;
    @FXML
    private Text newCustomerNumberText;
    @FXML
    private TextField newCustomerNameTextField;
    @FXML
    private TextField newCustomerAddressTextField;
    @FXML
    private TextField newCustomerCityTextField;
    @FXML
    private TextField newCustomerZipTextField;
    @FXML
    private TextField newCustomerCountryTextField;
    @FXML
    private TextField newCustomerNumberTextField;

    @FXML
    private Button addCustomerCancelButton;
    @FXML
    private Button addCustomerCreateButton;

    public void createCustomer() {
        ResultSet rs;
        if(!validateEmptyInputsAddNewCustomer()) {
        AlertMessage.display("All Fields are required. Make sure Zip Code and Phone number are of correct format.");
        } else {
            try {
                // add a check if customer already exists, partial match for address line?

                // check if country exists
                PreparedStatement pstmt = DBConnection.getConnection().prepareStatement("SELECT country FROM country WHERE country.country=?");
                pstmt.setString(1, newCustomerCountryTextField.getText());
                rs = pstmt.executeQuery();
                // if country that user specified doesn't exists, create a new country
                if (!rs.next()) {
                    System.out.println("Country doesn't exist");
                    try {
//                        TODO: research statement vs prepared statement
                        PreparedStatement createCountryPstmt = DBConnection.getConnection().prepareStatement("INSERT INTO countr (country, createDate, createdBy, lastUpdateBy) values (?, NOW(), ?, ?)");
                        createCountryPstmt.setString(1, newCustomerCountryTextField.getText());
                        createCountryPstmt.setString(2, "admin");
                        createCountryPstmt.setString(3, "admin");

                        int countryCreated = createCountryPstmt.executeUpdate();
//                    how to check if country was created successfully
                    } catch (SQLException e) {
                        AlertMessage.display("There was an error when creating customer, please reenter values and try again.");
                        e.printStackTrace();
                    }
                } else {
//                    proceed with the rest: City, Address
                        System.out.println("Country exists");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validateEmptyInputsAddNewCustomer() {
        try {
            String cName = newCustomerNameTextField.getText();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        try {
            String cAddress = newCustomerAddressTextField.getText();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        try {
            String cCity = newCustomerCityTextField.getText();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        try {
            int cZip = Integer.parseInt(newCustomerZipTextField.getText());
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        try {
            String cCountry = newCustomerCountryTextField.getText();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        try {
            int cNumber = Integer.parseInt(newCustomerNumberTextField.getText());
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }





    // close window
    public void closeCustomerAddNew(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @Override

    public void initialize(URL url, ResourceBundle rb) {

    }
}
