package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import utilities.NewWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerModifyController implements Initializable {
    //TODO change all zipcode naming to postalCode
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



    public void initModifyCustomerData(Customer customer) {
        modifyCustomerNameTextField.setText(customer.getCustomerName());
        modifyCustomerAddressTextField.setText(customer.getCustomerAddress());
        modifyCustomerCityTextField.setText(customer.getCustomerCity());
        modifyCustomerZipCodeTextField.setText(customer.getCustomerZipCode());
        modifyCustomerCountryTextField.setText(customer.getCustomerCountry());
        modifyCustomerPhoneTextField.setText(customer.getCustomerPhoneNumber());
    }
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerModifyMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
