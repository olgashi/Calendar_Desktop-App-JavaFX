package utilities;

import javafx.collections.ObservableList;
import model.Appointment;
import model.Schedule;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Reports {
    public static Map<String, Long> appointmentTypesByMonth(Month month, int year) {
        Map<String, Long> appointmentsMap = null;
        ObservableList<Appointment> appointmentForGivenMonth = Schedule.combineAppointmentsByMonth(month, year);
        if (appointmentForGivenMonth != null) {
            appointmentsMap = appointmentForGivenMonth.stream()
                    .collect(Collectors.groupingBy(Appointment::getAppointmentType, Collectors.counting()));
        }
        return appointmentsMap;

    }

    public static int appointmentTotalByMonth(Month month, int year) {
        ObservableList<Appointment> totalAppointmentForGivenMonth = Schedule.combineAppointmentsByMonth(month, year);
        if (totalAppointmentForGivenMonth != null) return totalAppointmentForGivenMonth.size();
        else return 0;
    }

    public static Map<String, List<Appointment>> allAppointmentByConsultant() {
        Map<String, List<Appointment>> appointmentsByConsultant = null;
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        if (allAppointments != null) {
            appointmentsByConsultant = allAppointments.stream()
                    .collect(Collectors.groupingBy(Appointment::getAppointmentContact));
        }
        return appointmentsByConsultant;
    }

    public static Set<String> allExistingConsultants(){
        return allAppointmentByConsultant().keySet();
    }

    public static Set<String> allExistingAppointmentTypes(){
        Map<String, List<Appointment>> appointmentTypes = null;
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        if (allAppointments != null) {
            appointmentTypes = allAppointments.stream()
                    .collect(Collectors.groupingBy(Appointment::getAppointmentType));
        }
        return appointmentTypes.keySet();
    }
}
