package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstPageController implements Initializable {

    @FXML
    private Button button_login;

    @FXML
    private Button button_signUp;
    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().isEmpty() && !tf_password.getText().isEmpty()){
                    boolean loggedIn = DBUtils.logInUser(tf_username.getText(), tf_password.getText());
                    if (loggedIn){
                        Navigator.changeScene(event, "logged-in.fxml","Welcome!");
                    } else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the credentials");
                    alert.show();
                }
            }
        });

        button_signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "sign-up.fxml", "Sign Up!");
            }
        });


    }
}
