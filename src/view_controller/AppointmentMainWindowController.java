package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import utilities.NewWindow;
import utilities.dbQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentMainWindowController implements Initializable {
    @FXML
    private Text appointmentMainWindowLabel;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, String> appointmentDescription;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, String> appointmentContact;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, String> appointmentUrl;
    @FXML
    private TableColumn<Appointment, String> appointmentStart;
    @FXML
    private TableColumn<Appointment, String> appointmentEnd;
    @FXML
    private TableColumn<Appointment, String> appointmentCustomerName;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button returnToMainWindowButton;
// TODO:This method is also in CustomerMainWindow Controller, extract?,
//  look if can grab info from event and load an appropriate file based on that (also look at openAddAppointmentWindow())
    @FXML
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void openAddAppointmentWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentAddNew.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Appointment.clearAppointmentList();
        dbQuery.createQuery("SELECT title, description, location, contact, type, url, start, end, customerName, customer.customerId FROM appointment, customer");
        ResultSet rs = dbQuery.getQueryResultSet();
//TODO refactor this
        while(true) {
            try{
                if (!rs.next()) break;
                Appointment.getAppointmentList().add(new Appointment(rs.getString("title"),
                        rs.getString("description"), rs.getString("location"),
                        rs.getString("contact"), rs.getString("type"),
                        rs.getString("url"), rs.getString("start"),
                        rs.getString("end"),
                        rs.getString("customerId"),
                        rs.getString("customerName")));
//                System.out.println(Appointment.getAppointmentList().get(0).toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentUrl.setCellValueFactory(new PropertyValueFactory<>("appointmentUrl"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentCustomerName.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        appointmentTable.setItems(Appointment.getAppointmentList());
    }
}
