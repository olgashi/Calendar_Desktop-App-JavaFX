package view_controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class AppointmentsPane extends AnchorPane {

    public AppointmentsPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppointmentsPane.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
