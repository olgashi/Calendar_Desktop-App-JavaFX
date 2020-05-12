package view_controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;
import utilities.NewWindow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CalendarMainWindowController implements Initializable {
    @FXML
    private Button viewByMonthButton;
    @FXML
    private Button viewByweekButton;
    @FXML
    private Button nextTimeframe;
    @FXML
    private Button previousTimeFrame;
    @FXML
    private Pane byMonthPane;
    @FXML
    private Pane byWeekPane;
    @FXML
    private GridPane byMonthGridPane;
    @FXML
    private GridPane byWeekGridPane;
    @FXML
    private Button returnToMainWindowButton;
    @FXML
    private Label calendarMainWindowLabel;
    @FXML
    private Label sundayLabel;
    @FXML
    private Label mondayLabel;
    @FXML
    private Label tuesdayLabel;
    @FXML
    private Label wednesdayLabel;
    @FXML
    private Label thursdayLabel;
    @FXML
    private Label fridayLabel;
    @FXML
    private Label saturdayLabel;
    @FXML
    private Text currentMonthLabel;
    @FXML
    private Text currentWeekLabel;

    LocalDate currentDate = LocalDate.now();
    int thisYearInt = currentDate.getYear();
    String thisYear = String.valueOf(currentDate.getYear());
    Month thisMonth = currentDate.getMonth();
    String thisDay = String.valueOf(currentDate.getDayOfMonth());
    boolean byWeekTimeFrame;

    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) calendarMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }

    public void showCalendarByWeek(ActionEvent event) {
        byWeekTimeFrame = true;
        byMonthPane.setVisible(false);
        byWeekPane.setVisible(true);
        currentMonthLabel.setText(String.valueOf(thisMonth));
        int daysSinceSunday = currentDate.getDayOfWeek().getValue();
        int weekStartDay = currentDate.getDayOfMonth() - daysSinceSunday;
        int weekEndDay = weekStartDay + 7;
        currentWeekLabel.setText(thisMonth + " " + weekStartDay + " - " + thisMonth + " " + weekEndDay + ", " + thisYear);
    }
    public void showCalendarByMonth(ActionEvent event) {
        byWeekTimeFrame = false;
        byWeekPane.setVisible(false);
        byMonthPane.setVisible(true);
        currentMonthLabel.setText(String.valueOf(thisMonth));
        currentWeekLabel.setText("");
    }

    public void nextTimeframe(ActionEvent event) {
        if (byWeekTimeFrame){

        } else {
            resetGridLines(byMonthGridPane);
           populateCalendar(currentMonthLabel, "incr");
        }
        return;
    }

    public void previousTimeframe(ActionEvent event) {
        if (byWeekTimeFrame){


        } else {
            resetGridLines(byMonthGridPane);
            populateCalendar(currentMonthLabel, "decr");
        }
        return;

    }

    public void resetGridLines(GridPane grid){
        Node gridLines = grid.getChildren().get(0);
        byMonthGridPane.getChildren().clear();
        byMonthGridPane.getChildren().add(0, gridLines);
    }

    public void populateCalendar(Text currentTimeFrameLabel, String direction) {
        if (byWeekTimeFrame){

        } else {
            resetGridLines(byMonthGridPane);
            int row = 0;
            int col;

            Month thisMonth = Month.valueOf(currentTimeFrameLabel.getText());
            Month nextMonth = direction.equals("incr") ? thisMonth.plus(1) : thisMonth.minus(1);
            if (thisMonth.equals(Month.DECEMBER) && nextMonth.equals(Month.JANUARY)) thisYearInt += 1;
            if (nextMonth.equals(Month.DECEMBER) && thisMonth.equals(Month.JANUARY)) thisYearInt -= 1;

            currentTimeFrameLabel.setText(nextMonth.toString());
            LocalDate dte = LocalDate.of(currentDate.getYear(), nextMonth, 1);
            int firstDayOfTheMonth = dte.getDayOfWeek().getValue();
            int totalDaysInMonth = dte.lengthOfMonth();
            ObservableList<Appointment> appointmentsForGivenMonth = Schedule.combineAppointmentsByMonth(nextMonth, thisYearInt);
//            System.out.println("Appointments: " + appointmentsForGivenMonth);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
//TODO optimize this code, make it faster
            col = firstDayOfTheMonth;
            if (appointmentsForGivenMonth != null) {
                for (int i = 1; i <= totalDaysInMonth; i++){
                    if (col == 7) {
                        row += 1;
                        col = 0;
                    }
                    for (int j = 0; j < appointmentsForGivenMonth.size(); j++) {
                        String temp[] = appointmentsForGivenMonth.get(j).getAppointmentStart().split(" ");
                        LocalDate localDate = LocalDate.parse(temp[0], formatter);
                        int apptDay = localDate.getDayOfMonth();
                        if (apptDay == i) {
                            Text test = new Text();
                            test.setText("Appointment with: " + appointmentsForGivenMonth.get(j).getAppointmentCustomerName());
                            GridPane.setConstraints(test, col, row);
                            byMonthGridPane.getChildren().addAll(test);
                        }
                    }
                    col += 1;
                }
            }

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        byWeekPane.setVisible(false);
        byMonthPane.setVisible(true);
        byWeekTimeFrame = false;
        currentMonthLabel.setText(thisMonth.minus(1).toString());
        currentWeekLabel.setText("");
        populateCalendar(currentMonthLabel, "incr");
    }
}
