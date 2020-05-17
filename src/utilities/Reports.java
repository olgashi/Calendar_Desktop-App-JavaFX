package utilities;

import javafx.collections.ObservableList;
import model.Appointment;
import model.Schedule;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Reports {
    public static Map<String, Long> typesByMonth(Month month, int year) {
        Map<String, Long> appointmentsMap = null;
        ObservableList<Appointment> appointmentForGivenMonth = Schedule.combineAppointmentsByMonth(month, year);
        if (appointmentForGivenMonth != null) {
            System.out.println(appointmentForGivenMonth);
            appointmentsMap = appointmentForGivenMonth.stream()
                    .collect(Collectors.groupingBy(Appointment::getAppointmentType, Collectors.counting()));
        }
        return appointmentsMap;

    }
}
