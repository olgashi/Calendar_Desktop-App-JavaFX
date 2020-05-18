package controller;

import javafx.collections.ObservableList;
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
import model.Schedule;
import utilities.*;
import java.io.IOException;
import java.net.URL;
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
                getClass().getResource("/view/MainWindow.fxml"));
    }

    public void openAddAppointmentWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentAddNew.fxml"));
    }
    public void deleteAppointment() throws SQLException {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            if (AlertMessage.display("Are you sure you want to delete appointment with " + appointment.getAppointmentCustomerName(), "confirmation")){
                DBQuery.createQuery("DELETE FROM appointment WHERE appointmentId = " + "'" + appointment.getAppointmentId()  + "'");
                Schedule.deleteAppointment(appointment);
            }
        }
        else AlertMessage.display("Please select appointment you want to delete", "warning");
    }

    @FXML
    private void loadMainWindow() throws IOException {
        NewWindow.display((Stage) appointmentMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentMainWindowController.fxml"));
    }

    public void openModifyAppointmentWindow(ActionEvent event) throws IOException {
        //TODO refactor this if possible to still use NewWindow.display
        Appointment appointment = null;
        try {
            appointment = appointmentTable.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            e.getSuppressed();
        }
        if (appointment!= null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/AppointmentModify.fxml"));
                Parent mainViewParent = loader.load();
                Scene modifyAppointmentView = new Scene(mainViewParent);
                AppointmentModifyController controller = loader.getController();
                controller.initModifyAppointmentData(appointment);
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
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentUrl.setCellValueFactory(new PropertyValueFactory<>("appointmentUrl"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentCustomerName.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        appointmentTable.setItems(allAppointments);
    }
}