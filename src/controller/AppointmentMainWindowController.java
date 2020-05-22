package controller;

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
    private Text apptMainWindowLabel;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, String> apptTitle;
    @FXML
    private TableColumn<Appointment, String> apptDescription;
    @FXML
    private TableColumn<Appointment, String> apptLocation;
    @FXML
    private TableColumn<Appointment, String> apptContact;
    @FXML
    private TableColumn<Appointment, String> apptType;
    @FXML
    private TableColumn<Appointment, String> apptStart;
    @FXML
    private TableColumn<Appointment, String> apptEnd;
    @FXML
    private TableColumn<Appointment, String> apptCustomerName;
    @FXML
    private Button addApptButton;
    @FXML
    private Button modifyApptButton;
    @FXML
    private Button deleteApptButton;
    @FXML
    private Button returnToMainWindowButton;

    @FXML
    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) apptMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/MainWindow.fxml"));
    }

    public void openAddAppointmentWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) apptMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentAddNew.fxml"));
    }

    public void deleteAppointment() throws SQLException {
        Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            if (AlertMessage.display("Are you sure you want to delete appointment with " + appointment.getAppointmentCustomerName() + "?", "confirmation")){
                DBQuery.createQuery("DELETE FROM appointment WHERE appointmentId = " + "'" + appointment.getAppointmentId()  + "'");
                Schedule.deleteAppointment(appointment);
            }
        }
        else AlertMessage.display("Please select appointment you want to delete", "warning");
    }

    @FXML
    private void loadMainWindow() throws IOException {
        NewWindow.display((Stage) apptMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/AppointmentMainWindowController.fxml"));
    }

    public void openModifyAppointmentWindow(ActionEvent event) throws IOException {
        Appointment appointment = null;
        try {
            appointment = apptTable.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        apptDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        apptLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        apptContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        apptStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        apptEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        apptCustomerName.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        apptTable.setItems(Appointment.getAppointmentList());
    }
}
