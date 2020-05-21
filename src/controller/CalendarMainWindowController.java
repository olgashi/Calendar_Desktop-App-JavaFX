package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Customer;
import model.Schedule;
import utilities.Calendar;
import utilities.NewWindow;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CalendarMainWindowController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, String> appointmentConsultant;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, String> appointmentStart;
    @FXML
    private TableColumn<Appointment, String> appointmentEnd;
    @FXML
    private TableColumn<Appointment, String> appointmentCustomerName;
    @FXML
    private Button viewByMonthButton;
    @FXML
    private Button viewByweekButton;
    @FXML
    private Button nextTimeframe;
    @FXML
    private Button previousTimeFrame;
    @FXML
    private Button returnToMainWindowButton;
    @FXML
    private Label calendarMainWindowLabel;
    @FXML
    private Text currentTimeFrameLabel;
    @FXML
    private Text currentWeekLabel;

    LocalDate currentDate = LocalDate.now();
    int thisYearInt = currentDate.getYear();
    Month thisMonth = currentDate.getMonth();
    Month nextMonth = thisMonth;
    boolean byWeekTimeFrame;

    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) calendarMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/MainWindow.fxml"));
    }

    public void incrementDecrementYear(Month thisMonth, Month nextMonth) {
        if (thisMonth.equals(Month.DECEMBER) && nextMonth.equals(Month.JANUARY)) thisYearInt += 1;
        if (nextMonth.equals(Month.DECEMBER) && thisMonth.equals(Month.JANUARY)) thisYearInt -= 1;
    }
    public String calculateWeekRange(LocalDate currentDate){
        int daysSinceSunday = currentDate.getDayOfWeek().getValue();
        LocalDate weekStartDay = currentDate.minus(Period.ofDays(daysSinceSunday));
        LocalDate weekEndDay = weekStartDay.plus(Period.ofDays(6));
        String formattedStringStart = weekStartDay.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
        String formattedStringEnd = weekEndDay.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
        return formattedStringStart + " - " + formattedStringEnd;
    }


    public void calendarByWeekOnClick(){
        byWeekTimeFrame = true;
        currentDate = currentDate.plus(Period.ofDays(6));
        currentTimeFrameLabel.setText(calculateWeekRange(currentDate.plus(Period.ofDays(6))));
        combineApptAndPopulateTable();
    }

    public void combineApptAndPopulateTable(){
        String[] weekRangeSplit = currentTimeFrameLabel.getText().split(" - ");
        LocalDate parsedStartDate = LocalDate.parse(weekRangeSplit[0], DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
        LocalDate parsedEndDate = LocalDate.parse(weekRangeSplit[1], DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
        appointmentTable.setItems(Schedule.combineAppointmentsByWeek(parsedStartDate, parsedEndDate));
    }

    public void calendarByMonthOnClick(){
        byWeekTimeFrame = false;
        currentTimeFrameLabel.setText(nextMonth.toString());
        appointmentTable.setItems(Schedule.combineAppointmentsByMonth(nextMonth, thisYearInt));
    }
    public void nextTimeFrame(ActionEvent event) {
        if (byWeekTimeFrame){
            currentDate = currentDate.plus(Period.ofDays(6));
            currentTimeFrameLabel.setText(calculateWeekRange(currentDate.plus(Period.ofDays(6))));
            combineApptAndPopulateTable();
        } else {
            thisMonth = Month.valueOf(currentTimeFrameLabel.getText());
            nextMonth = thisMonth.plus(1);
            incrementDecrementYear(thisMonth, nextMonth);
            currentTimeFrameLabel.setText(nextMonth.toString());
            appointmentTable.setItems(Schedule.combineAppointmentsByMonth(nextMonth, thisYearInt));
        }
    }

    public void previousTimeFrame(ActionEvent event) {
        if (byWeekTimeFrame){
            currentDate = currentDate.minus(Period.ofDays(6));
            currentTimeFrameLabel.setText(calculateWeekRange(currentDate.minus(Period.ofDays(6))));
            combineApptAndPopulateTable();
        } else {
            thisMonth = Month.valueOf(currentTimeFrameLabel.getText());
            nextMonth = thisMonth.minus(1);
            incrementDecrementYear(thisMonth, nextMonth);
            appointmentTable.setItems(Schedule.combineAppointmentsByMonth(nextMonth, thisYearInt));
            currentTimeFrameLabel.setText(nextMonth.toString());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byWeekTimeFrame = false;
        currentTimeFrameLabel.setText(thisMonth.toString());
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentConsultant.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentCustomerName.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        appointmentTable.setItems(Schedule.combineAppointmentsByMonth(thisMonth, thisYearInt));
    }
}
