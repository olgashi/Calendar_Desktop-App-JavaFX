package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointment {
    private SimpleStringProperty appointmentId;
    private SimpleStringProperty appointmentTitle;
    private SimpleStringProperty appointmentDescription;
    private SimpleStringProperty appointmentLocation;
    private SimpleStringProperty appointmentContact;
    private SimpleStringProperty appointmentType;
    private SimpleStringProperty appointmentUrl;
    private SimpleStringProperty appointmentStart;
    private SimpleStringProperty appointmentEnd;
    private SimpleStringProperty appointmentCustomerName;
    private SimpleStringProperty appointmentCustomerId;


    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public Appointment(String appId, String appTitle, String appDescription, String appLocation, String appContact,
                    String appType, String appUrl, String appStart, String appEnd, String customerId, String customerName) {
        this.appointmentId = new SimpleStringProperty(appId);
        this.appointmentTitle = new SimpleStringProperty(appTitle);
        this.appointmentDescription = new SimpleStringProperty(appDescription);
        this.appointmentLocation = new SimpleStringProperty(appLocation);
        this.appointmentContact = new SimpleStringProperty(appContact);
        this.appointmentType = new SimpleStringProperty(appType);
        this.appointmentUrl = new SimpleStringProperty(appUrl);
        this.appointmentStart = new SimpleStringProperty(appStart);
        this.appointmentEnd = new SimpleStringProperty(appEnd);
        this.appointmentCustomerId = new SimpleStringProperty(customerId);
        this.appointmentCustomerName = new SimpleStringProperty(customerName);
    }
    //TODO move to schedule class
    public static ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }
    public String getAppointmentId() {
        return appointmentId.get();
    }
    public void setAppointmentId(String appId) {
        appointmentId.set(appId);
    }
    public String getAppointmentTitle() {
        return appointmentTitle.get();
    }
    public void setAppointmentTitle(String appTitle) {
        appointmentTitle.set(appTitle);
    }

    public String getAppointmentDescription() {
        return appointmentDescription.get();
    }

    public void setAppointmentDescription(String appDescription) {
        appointmentDescription.set(appDescription);
    }

    public String getAppointmentLocation() {
        return appointmentLocation.get();
    }

    public void setAppointmentLocation(String appLocation) {
        appointmentLocation.set(appLocation);
    }

    public String getAppointmentContact() {
        return appointmentContact.get();
    }

    public void setAppointmentContact(String appContact) {
        appointmentContact.set(appContact);
    }

    public String getAppointmentType() {
        return appointmentType.get();
    }

    public void setAppointmentType(String appType) {
        appointmentType.set(appType);
    }

    public String getAppointmentUrl() {
        return appointmentUrl.get();
    }

    public void setAppointmentUrl(String appUrl) {
        appointmentUrl.set(appUrl);
    }

    public String getAppointmentStart() {
        return appointmentStart.get();
    }

    public void setAppointmentStart(String appStart) {
        appointmentStart.set(appStart);
    }

    public String getAppointmentEnd() {
        return appointmentEnd.get();
    }

    public void setAppointmentEnd(String appEnd) {
        appointmentEnd.set(appEnd);
    }

    public String getAppointmentCustomerName() {
        return appointmentCustomerName.get();
    }

    public void setAppointmentCustomerName(String appCustName) {
        appointmentCustomerName.set(appCustName);
    }

    public String getAppointmentCustomerId() {
        return appointmentCustomerId.get();
    }

    public void setAppointmentCustomerId(String appCustId) {
        appointmentCustomerId.set(appCustId);
    }
}
