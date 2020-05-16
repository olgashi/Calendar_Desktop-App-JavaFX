package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;

public class NewWindow {
    private static Stage stage;
    private static Parent root;
    private static Scene scene;
    public static void display(Stage stage, URL windowName) throws IOException {
        NewWindow.stage = stage;
        NewWindow.root = FXMLLoader.load(windowName);
        NewWindow.scene = new Scene(root);
        NewWindow.stage.setScene(scene);
        NewWindow.stage.show();
    }
}
