package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utilities.NewWindow;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private AnchorPane mainWindowView;
    @FXML
    private Button customersMainWindowViewButton;
    @FXML
    private Button appointmentsMainWindowViewButton;
    @FXML
    private Button reportsMainWindowViewButton;

    @FXML
    private void loadCustomersWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("CustomersMainWindow.fxml"));
    }

    @FXML
    private void loadAppointmentsWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) mainWindowView.getScene().getWindow(),
                getClass().getResource("AppointmentsMainWindow.fxml"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
