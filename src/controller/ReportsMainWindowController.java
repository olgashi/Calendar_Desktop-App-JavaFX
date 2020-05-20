package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;
import utilities.AlertMessage;
import utilities.NewWindow;
import utilities.Reports;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
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
    private ComboBox consultantNamesComboBox;
    @FXML
    private AnchorPane reportsWindow;
    @FXML
    private Text yearText;
    @FXML
    private Text secondComboBoxText;
    @FXML
    private Text reportNameHeader;
    @FXML
    private Pane reportsPane;
    @FXML
    private TableView<Appointment> consultantScheduleTable;
    @FXML
    private TableColumn<Appointment, String> name;
    @FXML
    private TableColumn<Appointment, String> customer;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> end;

    Month[] months = { Month.DECEMBER, Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
            Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER };
    private final String apptTypesReport = "Types by month";
    private final String apptTotalReport = "Total by month";
    private final String apptScheduleByConsultant = "Schedule by consultant";
    @FXML
    private void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) reportsWindow.getScene().getWindow(),
                getClass().getResource("/view/MainWindow.fxml"));
    }
    @FXML
    private void apptTypesReportOnSelect(){
        consultantScheduleTable.setVisible(false);
        reportNameHeader.setText("");
        yearText.setText("");
        if (reportListComboBox.getValue().equals(apptTypesReport) ||
                reportListComboBox.getValue().equals(apptTotalReport)) {
            yearListComboBox.setVisible(true);
            consultantNamesComboBox.setVisible(false);
            secondComboBoxText.setText("Pick a year");
            if (reportsPane.getChildren().size()>0) reportsPane.getChildren().clear();
        }
        else {
            reportsPane.getChildren().clear();
            if (reportListComboBox.getValue().equals(apptScheduleByConsultant)) {
                consultantNamesComboBox.getItems().setAll(Reports.allAppointmentByConsultant().keySet());
                consultantNamesComboBox.setVisible(true);
                secondComboBoxText.setText("Pick a consultant");
            }
        }
    }

    @FXML
    private void showReport(){
        int year = 0;
        reportsPane.getChildren().clear();
        if (reportListComboBox.getValue() == null) {
            AlertMessage.display("Please pick report type", "warning");
            return;
        }
        String reportName = (String) reportListComboBox.getValue();
        if ((reportName.equals(apptTypesReport) || reportName.equals(apptTotalReport)) && yearListComboBox.getValue() == null)
        {
            AlertMessage.display("Please pick a year", "warning");
            return;
        }
        if (reportName.equals(apptScheduleByConsultant) && consultantNamesComboBox.getValue() == null){
            AlertMessage.display("Please pick a consultant", "warning");
            return;
        }
        if (yearListComboBox.isVisible()) year = Integer.parseInt((String) yearListComboBox.getValue());
        pickReportToShow(year, reportName);
    }

    private void pickReportToShow(int year, String reportName) {
        reportNameHeader.setText(reportName);
        switch (reportName) {
            case apptTypesReport:
                showAppointmentTypesByMonth(year);
                break;
            case apptTotalReport:
                showAppointmentTotalByMonth(year);
                break;
            case apptScheduleByConsultant:
                showScheduleForConsultant((String) consultantNamesComboBox.getValue());
            default:
                break;
        }
    }

    private void showAppointmentTypesByMonth(int year) {
        consultantScheduleTable.setVisible(false);
        reportsPane.setVisible(true);
        yearText.setText(String.valueOf(year));
        yearText.setVisible(true);
        int posIncrement = 30;
        for (Month month : months) {
        Map<String, Long> reportsForTheMonth = Reports.appointmentTypesByMonth(month, year);
            if (reportsForTheMonth!= null) {
                String apptTypeString = "";
                for (Map.Entry<String, Long> entry : reportsForTheMonth.entrySet()) {
                   apptTypeString += entry.getKey() + ": " + entry.getValue() + "  ";
                }
                String textToShow = month.toString() + ": " + apptTypeString;
                posIncrement = showReportOutput(posIncrement,textToShow);
            }
        }
    }

    private void showScheduleForConsultant(String consultantName){
        yearListComboBox.setVisible(false);
        int posIncrement = 30;
        Map<String, List<Appointment>> allAppointments = Reports.allAppointmentByConsultant();
        List<Appointment> appointmentsForConsultant = allAppointments.get(consultantName);
        ObservableList<Appointment> list = FXCollections.observableArrayList(appointmentsForConsultant);
        appointmentsForConsultant.sort(Comparator.comparing(appt -> appt.getAppointmentStart()));
//        for(Appointment appt: appointmentsForConsultant){
//            String textToShow = appt.getAppointmentType() + " with " + appt.getAppointmentCustomerName() + " on " + appt.getAppointmentStart();
//            posIncrement = showReportOutput(posIncrement, textToShow);
//        }
        consultantScheduleTable.setVisible(true);
        reportsPane.setVisible(false);
        name.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        customer.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        start.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        end.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        consultantScheduleTable.setItems(list);
    }


    private void showAppointmentTotalByMonth(int year) {
        consultantScheduleTable.setVisible(false);
        yearText.setText(String.valueOf(year));
        yearText.setVisible(true);
        reportsPane.setVisible(true);
        int posIncrement = 30;
        for (Month month : months) {
            int apptTotal = Reports.appointmentTotalByMonth(month, year);
            //TODO extract to a method?
            String textToShow = "Appointment total for the month of " + month.toString() + ": " + apptTotal;
            posIncrement = showReportOutput(posIncrement, textToShow);
        }
    }

    private int showReportOutput(int posIncrement, String textToShow) {
        Text reportText = new Text();
        reportText.setText(textToShow);
        reportText.setX(0);
        reportText.setY(10 + posIncrement);
        reportsPane.getChildren().addAll(reportText);
        posIncrement += 23;
        return posIncrement;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consultantScheduleTable.setVisible(false);
        reportListComboBox.getItems().addAll(apptTypesReport, apptTotalReport, apptScheduleByConsultant);
        yearListComboBox.getItems().addAll(Schedule.existingYears());
        yearListComboBox.setVisible(false);
        consultantNamesComboBox.setVisible(false);
        secondComboBoxText.setText("");
    }
}

