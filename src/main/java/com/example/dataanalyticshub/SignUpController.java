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

public class SignUpController implements Initializable {
    @FXML
    private Button button_signUp;
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_firstName;
    @FXML
    private TextField tf_lastName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        button_signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!tf_firstName.getText().trim().isEmpty() && !tf_lastName.getText().trim().isEmpty() && !tf_username.getText().trim().isEmpty() &&!tf_password.getText().trim().isEmpty()){
                    DBUtils.signUpUser(event,tf_firstName.getText(),tf_lastName.getText(), tf_username.getText(), tf_password.getText());
                } else{
                    System.out.println("Please fill in all the information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to sign up!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "first-page.fxml", "Log in!");
            }
        });

    }
}
