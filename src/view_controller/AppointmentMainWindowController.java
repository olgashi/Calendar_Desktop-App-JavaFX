package view_controller;

import javafx.beans.property.SimpleStringProperty;
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
//    @FXML
//    private TableColumn<Customer, String> customerName;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        Scene customersMainWindow = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(customersMainWindow);
        window.show();
    }

    public void openAddAppointmentWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AppointmentAddNew.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 430, 450);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Appointment.clearAppointmentList();
        dbQuery.createQuery("SELECT title, description, location, contact, type, url, start, end FROM U071A3.appointment");
        ResultSet rs = dbQuery.getQueryResultSet();

        while(true) {
//            try {
//                if (!rs.next()) break;
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            try{
                if (!rs.next()) break;
                Appointment.getAppointmentList().add(new Appointment(rs.getString("title"),
                        rs.getString("description"), rs.getString("location"),
                        rs.getString("contact"), rs.getString("type"),
                        rs.getString("url"), rs.getString("start"),
                        rs.getString("end")));
//                System.out.println(Appointment.getAppointmentList().get(0).toString());
//TODO:Contact column is not showing any data, investigate
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

        appointmentTable.setItems(Appointment.getAppointmentList());
//        System.out.println(Customer.getCustomerList());


    }

}



