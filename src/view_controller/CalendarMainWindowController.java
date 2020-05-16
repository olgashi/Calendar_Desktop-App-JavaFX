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
import java.time.Period;
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
    private Text currentTimeFrameLabel;
    @FXML
    private Text currentWeekLabel;

    LocalDate currentDate = LocalDate.now();
    int thisYearInt = currentDate.getYear();
    Month thisMonth = currentDate.getMonth();
    Month nextMonth;
    boolean byWeekTimeFrame;

    public void loadMainWindow(ActionEvent event) throws IOException {
        NewWindow.display((Stage) calendarMainWindowLabel.getScene().getWindow(),
                getClass().getResource("MainWindow.fxml"));
    }
    public void addAppointmentTextToCalendar(int row, int col, ObservableList<Appointment> appointmentsForGivenTimeFrame, int apptIndex, String weekOrMonth) {
        Text appointmentText = new Text();
        appointmentText.setText("Appointment with: " + appointmentsForGivenTimeFrame.get(apptIndex).getAppointmentCustomerName());
        GridPane.setConstraints(appointmentText, col, row);
        if (weekOrMonth.equals("week")) { byWeekGridPane.getChildren().addAll(appointmentText); }
        else byMonthGridPane.getChildren().addAll(appointmentText);
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

    public void showCalendar(ActionEvent event){
        String weekOrMonth = ((Button)event.getSource()).getText();
        boolean showByMonthGrid = weekOrMonth.equals("View Calendar by Month");
        boolean showByWeekGrid = weekOrMonth.equals("View Calendar by Week");
        byWeekTimeFrame = weekOrMonth.equals("View Calendar by Week");
        byMonthPane.setVisible(showByMonthGrid);
        byWeekPane.setVisible(showByWeekGrid);
        if (weekOrMonth.equals("View Calendar by Week")) currentTimeFrameLabel.setText(calculateWeekRange(currentDate));
        else currentTimeFrameLabel.setText(String.valueOf(thisMonth));
    }

    public void nextTimeFrame(ActionEvent event) {
        if (byWeekTimeFrame){
            resetGridLines(byWeekGridPane);
            currentDate = currentDate.plus(Period.ofDays(6));
            currentTimeFrameLabel.setText(calculateWeekRange(currentDate.plus(Period.ofDays(6))));
            populateCalendar(currentTimeFrameLabel, "incr");
        } else {
            resetGridLines(byMonthGridPane);
            populateCalendar(currentTimeFrameLabel, "incr");
        }
    }

    public void previousTimeFrame(ActionEvent event) {
        if (byWeekTimeFrame){
            resetGridLines(byWeekGridPane);
            currentDate = currentDate.minus(Period.ofDays(6));
            currentTimeFrameLabel.setText(calculateWeekRange(currentDate.minus(Period.ofDays(6))));
            populateCalendar(currentTimeFrameLabel, "decr");
        } else {
            resetGridLines(byMonthGridPane);
            populateCalendar(currentTimeFrameLabel, "decr");
        }
    }

    public void resetGridLines(GridPane grid){
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);
    }
    //TODO refactor this method, extract code into multiple small methods
    public void populateCalendar(Text currentTimeFrameLabel, String direction) {
        int row, col;
        row = 0;
        if (byWeekTimeFrame){
            String[] weekRangeSplit = currentTimeFrameLabel.getText().split(" - ");
            LocalDate parsedStartDate = LocalDate.parse(weekRangeSplit[0], DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
            LocalDate parsedEndDate = LocalDate.parse(weekRangeSplit[1], DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
            incrementDecrementYear(thisMonth, nextMonth);
            ObservableList<Appointment> appointmentsForGivenWeek = Schedule.combineAppointmentsByWeek(parsedStartDate, parsedEndDate);
            if (appointmentsForGivenWeek != null) {
                for (col = 1; col <= 7; col++){
                    for (int j = 0; j < appointmentsForGivenWeek.size(); j++) {
                        String[] appointmentStartSplit = appointmentsForGivenWeek.get(j).getAppointmentStart().split(" ");
                        LocalDate localDate = LocalDate.parse(appointmentStartSplit[0], DateTimeFormatter.ofPattern( "yyyy-MM-dd" ));
                        if (parsedStartDate.plusDays(col).equals(localDate)) addAppointmentTextToCalendar(row, col, appointmentsForGivenWeek, j, "week");
                    }
                }
            }
        } else {
            thisMonth = Month.valueOf(currentTimeFrameLabel.getText());
            nextMonth = direction.equals("incr") ? thisMonth.plus(1) : thisMonth.minus(1);
            incrementDecrementYear(thisMonth, nextMonth);
            currentTimeFrameLabel.setText(nextMonth.toString());
            LocalDate firstDayOfMonthDate = LocalDate.of(currentDate.getYear(), nextMonth, 1);
            int firstDayOfTheMonth = firstDayOfMonthDate.getDayOfWeek().getValue();
            ObservableList<Appointment> appointmentsForGivenMonth = Schedule.combineAppointmentsByMonth(nextMonth, thisYearInt);
            col = firstDayOfTheMonth;
            if (appointmentsForGivenMonth != null) {
                for (int i = 1; i <= firstDayOfMonthDate.lengthOfMonth(); i++){
                    if (col == 7) {
                        row += 1;
                        col = 0;
                    }
                    for (int apptIndx = 0; apptIndx < appointmentsForGivenMonth.size(); apptIndx++) {
                        String[] temp = appointmentsForGivenMonth.get(apptIndx).getAppointmentStart().split(" ");
                        LocalDate localDate = LocalDate.parse(temp[0], DateTimeFormatter.ofPattern( "yyyy-MM-dd" ));
                        if (localDate.getDayOfMonth() == i) {
                            addAppointmentTextToCalendar(row, col, appointmentsForGivenMonth, apptIndx, "month");
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
        currentTimeFrameLabel.setText(thisMonth.minus(1).toString());
        populateCalendar(currentTimeFrameLabel, "incr");
    }
}
