package model;

import com.sun.tools.javadoc.Start;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;


// we should only get customers from the database once and only do updates and deletes
// at the same time
//class Schedule would contain a list of appointments associated with customers, just like in software I project
// with Inventory class. Look at all the methods there and use it as an example.
public class Schedule {
    private static ObservableList<Appointment> allAppointments = observableArrayList();
    private static ObservableList<Customer> allCustomers = observableArrayList();

    // add new customer to schedule
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
    public static Appointment lookupAppointmentById(String appointmentId) {
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> appt.getAppointmentId().equals(appointmentId));
        if (appointments.size() > 0) return appointments.get(0);
        else return null;
    }
    // look up customer by name
    public ObservableList<Customer> lookupCustomerByName(String customerName) {
        FilteredList<Customer> customers = new FilteredList<>(Customer.getCustomerList(), pre -> true);
        customers.setPredicate(cust -> cust.getCustomerName().equals(customerName));
        if (customers.size() > 0) return customers;
        else return null;
    }
    // return all appointments for customer (by name)
    public ObservableList<Appointment> lookupAppointmentsByCustomerName(String customerName) {
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> appt.getAppointmentCustomerName().equals(customerName));
        if (appointments.size() > 0) return appointments;
        else return null;
    }

    public static void clearAppointmentList(){
        Appointment.getAppointmentList().clear();
    }
    public static ObservableList<Appointment> getAppointmentList() {
        return allAppointments;
    }
    public static ObservableList<Customer> getCustomerList() {
        return allCustomers;
    }

    public static Integer lookupCustomerWithHighestID() {
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
    public static Integer lookupAppointmentWithHighestID() {
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
// TODO combine to methods into one, combine just by timeframe

    public static ObservableList<Appointment> combineAppointmentsByMonth(Month month, int year){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> {
            String temp[] = appt.getAppointmentStart().split(" ");
            LocalDate localDate = LocalDate.parse(temp[0] , formatter);
            Month apptMonth = localDate.getMonth();
            int apptYear = localDate.getYear();
            if (apptMonth.equals(month) && apptYear == year) {
                return true;
            } else return false;
        });
        if (appointments.size() > 0) return appointments;
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
        if (appointments.size() > 0) return appointments;
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

    public static boolean overlappingAppointmentsCheck(LocalDateTime newApptStart){
        DateTimeFormatter existingAppointmentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        ObservableList<Appointment> allAppointments = Appointment.getAppointmentList();
        boolean overlappingAppt = false;
            for (int i = 0; i< allAppointments.size(); i++) {
                String appointmentStart = allAppointments.get(i).getAppointmentStart();
                String appointmentEnd = allAppointments.get(i).getAppointmentEnd();
                LocalDateTime existingApptStartParsed = LocalDateTime.parse(appointmentStart, existingAppointmentFormatter);
                LocalDateTime existingApptEndParsed = LocalDateTime.parse(appointmentEnd, existingAppointmentFormatter);
                LocalDate existingApptDate = existingApptStartParsed.toLocalDate();

                LocalTime existingApptStartTime = existingApptStartParsed.toLocalTime();
                LocalTime existingApptEndTime = existingApptEndParsed.toLocalTime();
                System.out.println(newApptStart.toLocalDate());
                System.out.println(existingApptDate);
                System.out.println(existingApptStartTime);
                System.out.println(existingApptEndTime);
                System.out.println(newApptStart.toLocalTime());

                if (existingApptDate.equals(newApptStart.toLocalDate()) &&
                        (((newApptStart.toLocalTime().isAfter(existingApptStartTime) &&
                                newApptStart.toLocalTime().isBefore(existingApptEndTime))) ||
                                ((newApptStart.toLocalTime().equals(existingApptStartTime) ||
                                        newApptStart.toLocalTime().equals(existingApptEndTime))))) {
                    System.out.println("Found an overlapping appointment");
                   overlappingAppt = true;
                }
            }
            return overlappingAppt;
        }
}
