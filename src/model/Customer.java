package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerAddress;
    private SimpleStringProperty customerCity;
    private SimpleStringProperty customerZipCode;
    private SimpleStringProperty customerCountry;
    private SimpleStringProperty customerPhoneNumber;

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public Customer(String cName, String cAddress, String cCity, String cZipCode, String cCountry, String cPhoneNumber) {
        this.customerName = new SimpleStringProperty(cName);
        this.customerAddress = new SimpleStringProperty(cAddress);
        this.customerCity = new SimpleStringProperty(cCity);
        this.customerZipCode = new SimpleStringProperty(cZipCode);
        this.customerCountry = new SimpleStringProperty(cCountry);
        this.customerPhoneNumber = new SimpleStringProperty(cPhoneNumber);
    }

    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    public String getCustomerName() {
        return customerName.get();
    }
    public void setCustomerName(String cName) {
        customerName.set(cName);
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }
    public void setCustomerAddress(String cAddress) {
        customerAddress.set(cAddress);
    }

    public String getCustomerCity() {
        return customerCity.get();
    }
    public void setCustomerCity(String cCity) {
        customerCity.set(cCity);
    }

    public String getCustomerZipCode() {
        return customerZipCode.get();
    }
    public void setCustomerZipCode(String cZipCode) {
        customerZipCode.set(cZipCode);
    }

    public String getCustomerCountry() {
        return customerCountry.get();
    }
    public void setCustomerCountry(String cCountry) {
        customerCountry.set(cCountry);
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber.get();
    }
    public void setCustomerPhoneNumber(String cPhoneNumber) {
        customerPhoneNumber.set(cPhoneNumber);
    }

    public static void clearCustomerList(){
        customerList.clear();
    }
}
