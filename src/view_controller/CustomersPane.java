package view_controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

    public class CustomersPane extends AnchorPane {

        public CustomersPane(){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomersPane.fxml"));

            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
