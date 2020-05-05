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
import model.Appointment;
import model.Customer;
import utilities.AlertMessage;
import utilities.HelperQuery;
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
//    TODO doublecheck all access modifiers and make sure they all make sense
    @FXML
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void openAddAppointmentWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentAddNew.fxml"));
    }
    public void deleteAppointment() {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            if (AlertMessage.display("Are you sure you want to delete appointment with " + appointment.getAppointmentCustomerName(), "confirmation")){
                dbQuery.createQuery("DELETE FROM appointment WHERE appointmentId = " + "'" + appointment.getAppointmentId()  + "'");
                loadAppointmentTableData();
            }
        }
        else AlertMessage.display("Please select appointment you want to delete", "warning");
    }

    public void loadAppointmentTableData() {
        Appointment.clearAppointmentList();
        HelperQuery.getAppointmentData();
//        dbQuery.createQuery("SELECT appointmentId, title, description, location, contact, type, url, start, end, " +
//                "customerName, customer.customerId FROM appointment, customer where appointment.customerId = customer.customerId");
//        ResultSet rs = dbQuery.getQueryResultSet();
//        try {
//            while (dbQuery.getQueryResultSet().next()) {
//                Appointment.getAppointmentList().add(new Appointment(rs.getString("appointmentId"),
//                        rs.getString("title"), rs.getString("description"), rs.getString("location"),
//                        rs.getString("contact"), rs.getString("type"), rs.getString("url"), rs.getString("start"),
//                        rs.getString("end"), rs.getString("customerId"), rs.getString("customerName")));
//            }
//        }  catch(SQLException e){
//            e.printStackTrace();
//        }
    }
    @FXML
    private void loadMainWindow() throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("AppointmentMainWindowController.fxml"));
    }

    public void openModifyAppointmentWindow(ActionEvent event) throws IOException {
        //TODO refactor this if possible to still use NewWindow.display

        String customerName = null;
        Appointment appointment = null;
        try {
            appointment = appointmentTable.getSelectionModel().getSelectedItem();
            customerName = appointment.getAppointmentCustomerName();
        } catch (NullPointerException e) {
            e.getSuppressed();
        }
        if (appointment!= null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AppointmentModify.fxml"));
                Parent mainViewParent = loader.load();
                Scene modifyAppointmentView = new Scene(mainViewParent);
                AppointmentModifyController controller = loader.getController();
                controller.initModifyAppointmentData(appointment, customerName);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(modifyAppointmentView);
                window.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else AlertMessage.display("Please select appointment in a table and then click 'Modify Appointment'.", "warning");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAppointmentTableData();

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
