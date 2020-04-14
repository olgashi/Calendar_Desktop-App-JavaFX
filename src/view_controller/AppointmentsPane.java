package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAction;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsPane extends AnchorPane {
    @FXML
    private Button btn;
    @FXML
    private TextField tfield;


    public AppointmentsPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppointmentsPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onButtonClick(ActionEvent event){
        System.out.println("In controller");
    }

}
