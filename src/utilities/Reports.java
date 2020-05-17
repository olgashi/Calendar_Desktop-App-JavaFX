package utilities;

import javafx.collections.ObservableList;
import model.Appointment;
import model.Schedule;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Reports {
    public static Set<String> typesByMonth(LocalDateTime apptStart) {
        ObservableList<Appointment> appointmentForGivenMonth = Schedule.combineAppointmentsByMonth(apptStart.getMonth(), apptStart.getYear());

        Map<String, Long> appointmentsMap = appointmentForGivenMonth.stream()
                .collect(Collectors.groupingBy(Appointment::getAppointmentType, Collectors.counting()));
        return appointmentsMap.keySet();

    }
}
