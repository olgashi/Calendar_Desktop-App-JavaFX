package view_controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import utilities.dbQuery;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;


public class AppointmentAddNewController implements Initializable {
    @FXML
    private Text appointmentAddNewMainWindowLabel;
    @FXML
    private Text newAppointmentTitleText;
    @FXML
    private Text newAppointmentDateText;
    @FXML
    private Text newAppointmentTimeText;
    @FXML
    private Text newAppointmentCustomerText;
    @FXML
    private Text newAppointmentLocationText;
    @FXML
    private Text newAppointmentTypeText;
    @FXML
    private Text newAppointmentDescriptionText;
    @FXML
    private DatePicker newAppointmentDatePicker;
    @FXML
    private TextField newAppointmentTimeTextField;
    @FXML
    private TextField newAppointmentTypeTextField;
    @FXML
    private TextField newAppointmentDescriptionTextField;
    @FXML
    private TextField newAppointmentTitleTextField;
    @FXML
    private TableView<ObservableList<String>>customerTable;
    @FXML
    private TableColumn<String,String> appointmentCustomerNameColumn;
    @FXML
    private TableView<String> locationTable;
    @FXML
    private TableColumn<String,String> appointmentLocationColumn;
    @FXML
    private Button addAppointmentCancelButton;
    @FXML
    private Button addAppointmentCreateButton;

    private ObservableList<String> locationList = observableArrayList();


// Populate Customer table, Location table on window load
    public void getDataForLocationTable() throws SQLException {

        dbQuery.createQuery("SELECT DISTINCT location FROM appointment");
        while(dbQuery.getQueryResultSet().next()){
            System.out.println(dbQuery.getQueryResultSet().getString("location"));
//            locationList.add(dbQuery.getQueryResultSet().getString("location"));
//            System.out.println(locationList.size());
            locationList.add(dbQuery.getQueryResultSet().getString("location"));
        }
    }


//Table is not populating
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getDataForLocationTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        locationTable.setItems(locationList);
    }
}
// TODO include info popups on hover for input fields
