package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;
import model.User;
import utilities.AlertMessage;
import utilities.AuditLog;
import utilities.NewWindow;
import utilities.Reports;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class ReportsMainWindowController implements Initializable {
    @FXML
    private Button seeReportButton;
    @FXML
    private Button returnToMainViewButton;
    @FXML
    private ComboBox reportListComboBox;
    @FXML
    private ComboBox yearListComboBox;
    @FXML
    private AnchorPane reportsWindow;

    private String report1 = "Get types by month";
    @FXML
    private void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) reportsWindow.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }
    @FXML
    private void showReport(){
        String reportName = (String) reportListComboBox.getValue();
        int year = Integer.parseInt((String) yearListComboBox.getValue());
        if (reportName.equals(report1)) {
//            Month[] months = {Month.DECEMBER, Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
//                    Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER};
            Month[] months = {Month.MAY};
            Text reportText = new Text();
            for (Month month : months) {
                if (!Reports.typesByMonth(month, year).equals(null)) {
                    reportText.setText(Reports.typesByMonth(month, year).toString());
                    reportText.setX(200);
                    reportText.setY(200);
                   reportsWindow.getChildren().addAll(reportText);
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reportListComboBox.getItems().addAll(report1, "Report 2", "Report 3", "Report 4");
        yearListComboBox.getItems().addAll("2018", "2019", "2020");
    }
}

