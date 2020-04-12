package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


import java.net.URL;
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



    @FXML
    public void onBtnAClick(){
        CustomersPane customersPane = new CustomersPane();
        mainViewBorderPane.setCenter(customersPane);
    }
    @FXML
    public void onBtnBClick(){
        AppointmentsPane contentB = new AppointmentsPane();
        mainViewBorderPane.setCenter(contentB);
    }

    @FXML
    public void onBtnCClick(){
        ReportsPane contentC = new ReportsPane();
        mainViewBorderPane.setCenter(contentC);
    }


    //    public void mainViewCustomersButtonClicked(ActionEvent event) {
//        System.out.println(mainViewStackPane.getChildren().size());
//        if (mainViewStackPane.getChildren().size() > 0) {
//            System.out.println("Inside the if statement");
//            System.out.println(mainViewStackPane.getChildren().size());
//            mainViewStackPane.getChildren().removeAll();
//            System.out.println(mainViewStackPane.getChildren().size());
//        }
//        mainViewStackPane.getChildren().add(mainViewCustomersPane);
//        System.out.println(mainViewStackPane.getChildren().size());
//    }
//
//
//
//    public void mainViewAppointmentsButtonClicked(ActionEvent event) {
//        if (mainViewStackPane.getChildren().size() > 0) {
//            mainViewStackPane.getChildren().removeAll();
//        }
//        mainViewStackPane.getChildren().add(mainViewAppointmentsPane);
//    }
//
//
//    public void mainViewReportsButtonClicked(ActionEvent event) {
//        if (mainViewStackPane.getChildren().size() > 0) {
//            mainViewStackPane.getChildren().removeAll();
//        }
//        mainViewStackPane.getChildren().add(mainViewReportsPane);
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<Node> paneChildren = mainViewStackPane.getChildren();

        mainViewStackPane.getChildren().remove(0,3);


    }
}
