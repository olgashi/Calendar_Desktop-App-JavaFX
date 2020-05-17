package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.NewWindow;
import utilities.Reports;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Map;
import java.util.ResourceBundle;

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
    @FXML
    private Pane reportsPane;

    private String apptTypesReport = "Get types by month";
    @FXML
    private void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) reportsWindow.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }
    //TODO limit number of appointment types to 3-4
    @FXML
    private void showReport(){
        reportsPane.getChildren().clear();
        String reportName = (String) reportListComboBox.getValue();
        int year = Integer.parseInt((String) yearListComboBox.getValue());
        if (reportName.equals(apptTypesReport)) {
            Month[] months = {Month.DECEMBER, Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
                    Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER};
            int posIncrement = 10;
            for (Month month : months) {
            Map<String, Long> reportsForTheMonth = Reports.typesByMonth(month, year);
                if (reportsForTheMonth!= null) {
                    Text reportText = new Text();
                    String apptTypeString = "Appointment types: ";
                    for (Map.Entry<String, Long> entry : reportsForTheMonth.entrySet()) {
                       apptTypeString += entry.getKey() + ", count: " + entry.getValue();
                    }
                    reportText.setText(month.toString() + ": " + apptTypeString);
                    reportText.setX(0);
                    reportText.setY(10 + posIncrement);
                   reportsPane.getChildren().addAll(reportText);
                   posIncrement += 23;
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reportListComboBox.getItems().addAll(apptTypesReport, "Report 2", "Report 3", "Report 4");
        yearListComboBox.getItems().addAll("2018", "2019", "2020");
    }
}

