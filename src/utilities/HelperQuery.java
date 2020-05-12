package utilities;

import model.Appointment;
import model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelperQuery {
    public static void getCustomerData () throws SQLException {
        DBQuery.createQuery("SELECT customerId, customerName, address, city, postalCode, country, phone FROM U071A3.customer, U071A3.address, U071A3.city, U071A3.country " +
                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
        ResultSet rs = DBQuery.getQueryResultSet();
        try {
            while(DBQuery.getQueryResultSet().next()) {
                Customer.getCustomerList().add(new Customer(rs.getString("customerId"), rs.getString("customerName"),
                        rs.getString("address"), rs.getString("city"), rs.getString("postalCode"),
                        rs.getString("country"), rs.getString("phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAppointmentData() throws SQLException {
//        Appointment.clearAppointmentList();
        DBQuery.createQuery("SELECT appointmentId, title, description, location, contact, type, url, start, end, " +
                "customerName, customer.customerId FROM appointment, customer where appointment.customerId = customer.customerId");
        ResultSet rs = DBQuery.getQueryResultSet();
        try {
            while (DBQuery.getQueryResultSet().next()) {
                Appointment.getAppointmentList().add(new Appointment(rs.getString("appointmentId"),
                        rs.getString("title"), rs.getString("description"), rs.getString("location"),
                        rs.getString("contact"), rs.getString("type"), rs.getString("url"), rs.getString("start"),
                        rs.getString("end"), rs.getString("customerId"), rs.getString("customerName")));
            }
        }  catch(SQLException e){
            e.printStackTrace();
        }
    }
}
