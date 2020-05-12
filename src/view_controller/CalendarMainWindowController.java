package view_controller;

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
import utilities.NewWindow;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
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
    private Text currentTimeFrame;
    LocalDate currentDate = LocalDate.now();
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
        currentTimeFrame.setText(thisMonth + " " + thisDay + ", " + thisYear);
    }
    public void showCalendarByMonth(ActionEvent event) {
        byWeekTimeFrame = false;
        byWeekPane.setVisible(false);
        byMonthPane.setVisible(true);
        currentTimeFrame.setText(String.valueOf(thisMonth));

    }

    public void nextTimeframe(ActionEvent event) {
        int row = 0;
        int col = 0;
        if (byWeekTimeFrame){


        } else {
//            byMonthGridPane.
            Node gridLines = byMonthGridPane.getChildren().get(0);
            byMonthGridPane.getChildren().clear();
            byMonthGridPane.getChildren().add(0, gridLines);
            Month thisMonth = Month.valueOf(currentTimeFrame.getText());
            Month nextMonth = thisMonth.plus(1);
            currentTimeFrame.setText(nextMonth.toString());
            LocalDate dte = LocalDate.of(currentDate.getYear(), nextMonth, 1);
            int firstDayOfTheMonth = dte.getDayOfWeek().getValue();
            int totalDaysInMonth = dte.lengthOfMonth();
            System.out.println("Total: " + totalDaysInMonth);
            col = firstDayOfTheMonth;
            for (int i = 1; i <= totalDaysInMonth; i++){
                if (col == 7) {
                    row += 1;
                    col = 0;
                }

                Text test = new Text();
                test.setText(i + "of " + dte.getMonth());
                byMonthGridPane.setConstraints(test, col, row);

                col += 1;

                byMonthGridPane.getChildren().addAll(test);
            }
        }
        return;

    }

    public void previousTimeframe(ActionEvent event) {
        if (byWeekTimeFrame){


        } else {
            Node gridLines = byMonthGridPane.getChildren().get(0);
            byMonthGridPane.getChildren().clear();
            byMonthGridPane.getChildren().add(0, gridLines);
            Month thisMonth = Month.valueOf(currentTimeFrame.getText());
            Month previousMonth = thisMonth.minus(1);
            currentTimeFrame.setText(previousMonth.toString());
            LocalDate dte = LocalDate.of(currentDate.getYear(), previousMonth, 1);
            int firstDayOfTheMonth = dte.getDayOfWeek().getValue();
            System.out.println(firstDayOfTheMonth);
            Text test = new Text();
            test.setText("first day of the month");
            byMonthGridPane.setConstraints(test, firstDayOfTheMonth, 3);
            byMonthGridPane.getChildren().addAll(test);

        }
        return;

    }

    public void populateCalendar(ActionEvent event) {
        // determine what day of the week first day of the month falls on
        // populate first day of the month and then days before and days after
        // create a hashMap? where key is a date and value is indexes for row and col in the grid
        // combine appointments depending on the time frame:
        // if it is by week group all appointments for that week
        // if that is by month group all appointments for that month
        // sort appointments by day in that bucket
        // loop through the appointments in that bucket and compare day value to the grid
        if (byWeekTimeFrame){


        } else {

        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        byWeekPane.setVisible(false);
        byMonthPane.setVisible(true);
//        currentTimeFrame.setText(thisMonth + " " + thisDay + ", " + thisYear);
        byWeekTimeFrame = false;
        currentTimeFrame.setText(thisMonth.toString());

    }
}
