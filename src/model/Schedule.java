package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static javafx.collections.FXCollections.observableArrayList;

public class Schedule {
    private static ObservableList<Appointment> allAppointments = observableArrayList();
    private static ObservableList<Customer> allCustomers = observableArrayList();

    public static void addCustomer(Customer customer) {
        Customer.getCustomerList().add(customer);
    }

    // add new appointment to schedule
    public static void addAppointment(Appointment appointment) {
        Appointment.getAppointmentList().add(appointment);
    }

    // remove customer from allCustomers along with any appointments associated with that customer
    public static boolean deleteCustomer(Customer customer) {
        Customer foundCustomer = lookupCustomerById(customer.getCustomerId());
        if (foundCustomer != null) {
            Customer.getCustomerList().remove(customer);
            return true;
        } else return false;
    }

    // remove appointment by customer name
    public static boolean deleteAppointment(Appointment appointment) {
        Appointment foundAppointment = lookupAppointmentById(appointment.getAppointmentId());
        if (foundAppointment != null) {
            Appointment.getAppointmentList().remove(appointment);
            return true;
        } else return false;
    }

    // look up customer by id
    public static Customer lookupCustomerById(String customerId) {
        FilteredList<Customer> customers = new FilteredList<>(Customer.getCustomerList(), pre -> true);
        customers.setPredicate(cust -> cust.getCustomerId().equals(customerId));
        if (customers.size() > 0) return customers.get(0);
        else return null;
    }

    // look up appointment by id
    private static Appointment lookupAppointmentById(String appointmentId) {
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        //predicate lambda expression to efficiently look up appointment in appointment list (by appointment id)
        appointments.setPredicate(appt -> appt.getAppointmentId().equals(appointmentId));
        if (appointments.size() > 0) return appointments.get(0);
        else return null;
    }

    // look up customer by name
    public ObservableList<Customer> lookupCustomerByName(String customerName) {
        FilteredList<Customer> customers = new FilteredList<>(Customer.getCustomerList(), pre -> true);
        //predicate lambda expression to efficiently look up customer in customer list (by customer name)
        customers.setPredicate(cust -> cust.getCustomerName().equals(customerName));
        if (customers.size() > 0) return customers;
        else return null;
    }

    // return all appointments for customer (by name)
    public ObservableList<Appointment> lookupAppointmentsByCustomerName(String customerName) {
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        //predicate lambda expression to efficiently look up customer appointments in appointment list (by customer name)
        appointments.setPredicate(appt -> appt.getAppointmentCustomerName().equals(customerName));
        if (appointments.size() > 0) return appointments;
        else return null;
    }

    public static void clearAppointmentList(){
        Appointment.getAppointmentList().clear();
    }

    private static Integer lookupCustomerWithHighestID() {
        ObservableList<Customer> allCustomers = Customer.getCustomerList();
        if (allCustomers.size() > 0) {
            Customer max = allCustomers.get(0);
            for (int i = 0; i< allCustomers.size(); i++) {
                if (Integer.parseInt(allCustomers.get(i).getCustomerId()) > Integer.parseInt(max.getCustomerId())) {
                    max = allCustomers.get(i);
                }
            }
            return Integer.parseInt(max.getCustomerId());
        } else {
            return 0;
        }
    }

    private static Integer lookupAppointmentWithHighestID() {
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        if (allAppointments.size() > 0) {
            Appointment max = allAppointments.get(0);
            for (int i = 0; i< allAppointments.size(); i++) {
                if (Integer.parseInt(allAppointments.get(i).getAppointmentId()) > Integer.parseInt(max.getAppointmentId())) {
                    max = allAppointments.get(i);
                }
            }
            return Integer.parseInt(max.getAppointmentId());
        } else {
            return 0;
        }
    }

    public static String setCustomerId() {
        String id = String.valueOf(lookupCustomerWithHighestID() + 1);
        return id;
    }

    public static String setAppointmentId() {
        String id = String.valueOf(lookupAppointmentWithHighestID() + 1);
        return id;
    }

    public static ObservableList<Appointment> combineAppointmentsByMonth(Month month, int year){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> {
            String[] temp = appt.getAppointmentStart().split(" ");
            LocalDate localDate = LocalDate.parse(temp[0] , formatter);
            Month apptMonth = localDate.getMonth();
            int apptYear = localDate.getYear();
            if (apptMonth.equals(month) && apptYear == year) {
                return true;
            } else return false;
        });
        if (appointments.size() > 0) {
            ObservableList<Appointment> apptList = FXCollections.observableArrayList(appointments);
            apptList.sort(Comparator.comparing(Appointment::getAppointmentStart));
            return apptList;
        }
        else return null;
    }

    public static ObservableList<Appointment> combineAppointmentsByWeek(LocalDate start, LocalDate end){
        DateTimeFormatter existingAppointmentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> {
            String appointmentStart = appt.getAppointmentStart();
            LocalDateTime existingApptStartParsed = LocalDateTime.parse(appointmentStart, existingAppointmentFormatter);
            LocalDate existingApptDate = existingApptStartParsed.toLocalDate();
            if (existingApptDate.compareTo(start) >= 0 && existingApptDate.compareTo(end) <= 0) {
                return true;
            } else return false;
        });
        if (appointments.size() > 0) {
            ObservableList<Appointment> apptList = FXCollections.observableArrayList(appointments);
            apptList.sort(Comparator.comparing(Appointment::getAppointmentStart));
            return apptList;
        }
        else return null;
    }

    public static Appointment appointmentsWithinFifteenMinutes(LocalDateTime loginTime){
        DateTimeFormatter existingAppointmentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        Appointment found = null;
        for (int i = 0; i< allAppointments.size(); i++) {
            String appointmentStart = allAppointments.get(i).getAppointmentStart();
            LocalDateTime existingApptStartParsed = LocalDateTime.parse(appointmentStart, existingAppointmentFormatter);
            LocalDate existingApptDate = existingApptStartParsed.toLocalDate();
            LocalTime existingApptTime = existingApptStartParsed.toLocalTime();
            if (existingApptDate.equals(loginTime.toLocalDate()) &&
                    ((loginTime.toLocalTime().isBefore(existingApptTime) &&
                            loginTime.toLocalTime().plusMinutes(15).isAfter(existingApptTime)))) {
                found = allAppointments.get(i);
            }
        }
        return found;
    }

    public static boolean overlappingAppointmentsCheck(LocalDateTime newApptStart, int customerId, int apptId){
        DateTimeFormatter existingAppointmentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        boolean overlappingAppt = false;
            for (int i = 0; i< allAppointments.size(); i++) {
                Appointment appt =allAppointments.get(i);
                String appointmentStart = appt.getAppointmentStart();
                String appointmentEnd = appt.getAppointmentEnd();
                int appointmentCustomerId = Integer.parseInt(appt.getAppointmentCustomerId());
                LocalDateTime existingApptStartParsed = LocalDateTime.parse(appointmentStart, existingAppointmentFormatter);
                LocalDateTime existingApptEndParsed = LocalDateTime.parse(appointmentEnd, existingAppointmentFormatter);
                LocalDate existingApptDate = existingApptStartParsed.toLocalDate();
                LocalTime existingApptStartTime = existingApptStartParsed.toLocalTime();
                LocalTime existingApptEndTime = existingApptEndParsed.toLocalTime();
                if (apptId != Integer.parseInt(appt.getAppointmentId()) && customerId == appointmentCustomerId && existingApptDate.equals(newApptStart.toLocalDate()) &&
                        (((newApptStart.toLocalTime().isAfter(existingApptStartTime) &&
                                newApptStart.toLocalTime().isBefore(existingApptEndTime))) ||
                                ((newApptStart.toLocalTime().equals(existingApptStartTime) ||
                                        newApptStart.toLocalTime().equals(existingApptEndTime))))) {
                   overlappingAppt = true;
                }
            }
            return overlappingAppt;
        }

        public static boolean checkIfWithinNormalBusinessHours(LocalDateTime newAppptStart, LocalDateTime newApptEnd) {
            LocalTime start = newAppptStart.toLocalTime();
            LocalTime end = newApptEnd.toLocalTime();
            LocalTime businessHoursStart = LocalTime.parse( "08:59:00" );
            LocalTime businessHoursEnd = LocalTime.parse( "17:01:00" );
           return (start.isAfter(businessHoursStart)) && end.isBefore(businessHoursEnd);
        }

        public static List<String> existingYears(){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
            ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
            Set<String> years = new HashSet<>(Collections.singletonList(String.valueOf(LocalDateTime.now().getYear())));
            for (Appointment appt: allAppointments) {
                LocalDateTime existingApptEndParsed = LocalDateTime.parse(appt.getAppointmentEnd(), dateTimeFormatter);
                int year = existingApptEndParsed.getYear();
                if (!years.contains(String.valueOf(year))) {
                    years.add(String.valueOf(year));
                }
            }
            if (years.size() > 0) {
                List<String> yearsSorted = new ArrayList<>(years);
                Collections.sort(yearsSorted);
                return yearsSorted;
            } else return null;
    }
}
