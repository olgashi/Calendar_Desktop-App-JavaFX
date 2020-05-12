package model;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
            return null;
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
            return null;
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

    public static ObservableList<Appointment> combineAppointmentsByMonth(Month month){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> {
            String temp[] = appt.getAppointmentStart().split(" ");
            LocalDate localDate = LocalDate.parse(temp[0] , formatter);
            Month apptMonth = localDate.getMonth();
            if (apptMonth.equals(month)) {
                return true;
            } else return false;
        });
        if (appointments.size() > 0) return appointments;
        else return null;
    }

    public static ObservableList<Appointment> combineAppointmentsByWeek(Month month, int weekStartDay, int weekEndDay){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        FilteredList<Appointment> appointments = new FilteredList<>(Appointment.getAppointmentList(), pre -> true);
        appointments.setPredicate(appt -> {
            String temp[] = appt.getAppointmentStart().split(" ");
            LocalDate localDate = LocalDate.parse(temp[0] , formatter);
            int apptDay = localDate.getDayOfMonth();
            Month apptMonth = localDate.getMonth();
            if (apptMonth.equals(month) && apptDay >= weekStartDay && apptDay <= weekEndDay) {
                return true;
            } else return false;
        });
        if (appointments.size() > 0) return appointments;
        else return null;

    }
}
