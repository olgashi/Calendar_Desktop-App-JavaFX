package utilities;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Calendar {

    public static Callback<DatePicker, DateCell> customDayCellFactory() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate dte, boolean empty) {
                        super.updateItem(dte, empty);
                        if (dte.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                dte.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                dte.isBefore(LocalDateTime.now().toLocalDate())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }
}
