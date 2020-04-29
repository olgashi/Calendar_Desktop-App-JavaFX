package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
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
    private void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void openAddCustomerWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("CustomerAddNew.fxml"));
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Customer.clearCustomerList();
        dbQuery.createQuery("SELECT customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country " +
                "WHERE customer.addressId = address.addressId " +
                "AND address.cityId = city.cityId AND city.countryId = country.countryId");
        ResultSet rs = dbQuery.getQueryResultSet();
//TODO refactor this
        while(true) {
            try {
                if (!dbQuery.getQueryResultSet().next()) break;
                Customer.getCustomerList().add(new Customer(rs.getString("customerName"),
                        rs.getString("address"), rs.getString("city"),
                        rs.getString("postalCode"), rs.getString("country"), rs.getString("phone")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        customerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        customerCity.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerCity"));
        customerZipCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerZipCode"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerCountry"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPhoneNumber"));
        customerTable.setItems(Customer.getCustomerList());
    }
}
