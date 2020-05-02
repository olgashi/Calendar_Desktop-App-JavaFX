package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import utilities.AlertMessage;
import utilities.NewWindow;
import utilities.dbQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersMainWindowController implements Initializable {
    @FXML
    private Text customerMainWindowLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> customerId;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerCity;
    @FXML
    private TableColumn<Customer, String> customerZipCode;
    @FXML
    private TableColumn<Customer, String> customerCountry;
    @FXML
    private TableColumn<Customer, String> customerPhoneNumber;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Button returnToMainWindowButton;

    @FXML
    private void loadCustomerTableData (){
        Customer.clearCustomerList();
        dbQuery.createQuery("SELECT customerId, customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country " +
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
    @FXML
    private void loadMainWindow() throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void openAddCustomerWindow() throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomerAddNew.fxml"));
    }

    public void openModifyCustomerWindow(ActionEvent event) throws IOException {
        //TODO refactor this if possible to still use NewWindow.display
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("CustomerModify.fxml"));
                Parent mainViewParent = loader.load();
                Scene modifyCustomerView = new Scene(mainViewParent);
                CustomerModifyController controller = loader.getController();
                controller.initModifyCustomerData(customer);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(modifyCustomerView);
                window.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else AlertMessage.display("Please select customer in a table and then click 'Modify Customer'.", "warning");
    }

    public void deleteCustomer() {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if (customer != null) {
                if (AlertMessage.display("Are you sure you want to delete customer " + customer.getCustomerName(), "confirmation")){
                    dbQuery.createQuery("DELETE FROM customer WHERE customerName = " + "'" + customer.getCustomerName()  + "'");
                    loadCustomerTableData();
                }
            }
            else AlertMessage.display("Please select customer you want to delete", "warning");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCustomerTableData();
        // populate customer table
        customerId.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        customerCity.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerCity"));
        customerZipCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerZipCode"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerCountry"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPhoneNumber"));
        customerTable.setItems(Customer.getCustomerList());
    }
}
//TODO doublecheck all alertmessage's text
