package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Go back to the first page after user log out
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "first-page.fxml", "Log in", null);

            }
        });
    }

    public void setUserInformation (String username){
        label_welcome.setText("Welcome " + username + "!");
    }
}
