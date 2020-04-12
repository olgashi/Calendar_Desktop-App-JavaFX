package view_controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {
    @FXML
    private BorderPane mainViewBorderPane;
    @FXML
    private StackPane mainViewStackPane;
    @FXML
    private Pane mainViewCustomersPane;
    @FXML
    private Pane mainViewAppointmentsPane;
    @FXML
    private Pane mainViewReportsPane;
    @FXML
    private Button mainViewCustomersButton;
    @FXML
    private Button mainViewAppointmentsButton;
    @FXML
    private Button mainViewReportsButton;


    //
//
    public void mainViewCustomersButtonClicked(ActionEvent event) {
        if (mainViewStackPane.getChildren().isEmpty())  mainViewStackPane.getChildren().add(mainViewCustomersPane);
    }



    public void mainViewAppointmentsButtonClicked(ActionEvent event) {
        if (mainViewStackPane.getChildren().isEmpty())  mainViewStackPane.getChildren().add(mainViewAppointmentsPane);
    }


    public void mainViewReportsButtonClicked(ActionEvent event) {
        if (mainViewStackPane.getChildren().isEmpty())  mainViewStackPane.getChildren().add(mainViewReportsPane);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<Node> paneChildren = mainViewStackPane.getChildren();
        mainViewStackPane.getChildren().remove(0,3);


    }
}
