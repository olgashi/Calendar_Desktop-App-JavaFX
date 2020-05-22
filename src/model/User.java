package model;

import javafx.beans.property.SimpleStringProperty;

public class User {
    public static SimpleStringProperty userName;
    public static SimpleStringProperty userId;

    public User(String uName, String uId){
        userName = new SimpleStringProperty(uName);
        userId = new SimpleStringProperty(uId);
    }

    public User(){

    }

    public void setUserName(String uName) {
        userName.set(uName);
    }

    public static String getUserName() {
        return userName.get();
    }

    public void setUserId(String uName) {
        userId.set(uName);
    }

    public String getUserId() {
        return userId.get();
    }
}
