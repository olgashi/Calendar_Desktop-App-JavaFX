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
    private Text yearText;
    @FXML
    private Pane reportsPane;
    Month[] months = {Month.DECEMBER, Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
            Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER};
    private final String apptTypesReport = "Types by month";
    private final String apptTotalReport = "Total by month";
    @FXML
    private void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) reportsWindow.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }
    @FXML
    private void apptTypesReportOnSelect(){
        if (reportListComboBox.getValue().equals(apptTypesReport) ||
                reportListComboBox.getValue().equals(apptTotalReport)) {
            yearListComboBox.setVisible(true);
            if (reportsPane.getChildren().size()>0) reportsPane.getChildren().clear();

        }
        else {
            yearListComboBox.setVisible(false);
            reportsPane.getChildren().clear();
        }
    }

    @FXML
    private void showReport(){
        reportsPane.getChildren().clear();
        String reportName = (String) reportListComboBox.getValue();
        int year = Integer.parseInt((String) yearListComboBox.getValue());
        switch (reportName) {
            case apptTypesReport:
                showAppointmentTypesByMonth(year);
                break;
            case apptTotalReport:
                showAppointmentTotalByMonth(year);
                break;
            default:    //set default to spanish
                break;
        }
    }

    private void showAppointmentTypesByMonth(int year) {
        yearText.setText(String.valueOf(year));
        yearText.setVisible(true);
        int posIncrement = 30;
        for (Month month : months) {
        Map<String, Long> reportsForTheMonth = Reports.appointmentTypesByMonth(month, year);
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
    private void showAppointmentTotalByMonth(int year) {
        yearText.setText(String.valueOf(year));
        yearText.setVisible(true);
        int posIncrement = 30;
        for (Month month : months) {
            int apptTotal = Reports.appointmentTotalByMonth(month, year);
            Text reportText = new Text();
            reportText.setText("Appointment total for the month of " + month.toString() + ": " + apptTotal);
            reportText.setX(0);
            reportText.setY(10 + posIncrement);
            reportsPane.getChildren().addAll(reportText);
            posIncrement += 23;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reportListComboBox.getItems().addAll(apptTypesReport, apptTotalReport, "Report 3", "Report 4");
//        TODO create method that generates a list of years with appointments
        yearListComboBox.getItems().addAll("2020", "2019");
        yearListComboBox.setVisible(false);
//        yearText.setVisible(false);
    }
}

