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

public class EditProfileController implements Initializable {
    @FXML
    private Button button_saveChanges;
    @FXML
    private Button button_backToDashboard;
    @FXML
    private TextField tf_newUsername;
    @FXML
    private TextField tf_newPassword;
    @FXML
    private TextField tf_newFirstName;
    @FXML
    private TextField tf_newLastName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tf_newUsername.setText(DBUtils.getCurrentUser().getUsername());
        tf_newFirstName.setText(DBUtils.getCurrentUser().getFirstName());
        tf_newLastName.setText(DBUtils.getCurrentUser().getLastName());
        tf_newPassword.setText(DBUtils.getCurrentUser().getPassword());
        button_saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_newFirstName.getText().trim().isEmpty() && !tf_newLastName.getText().trim().isEmpty() && !tf_newUsername.getText().trim().isEmpty() && !tf_newPassword.getText().trim().isEmpty()) {
                    DBUtils.updateProfile(event, tf_newFirstName.getText(), tf_newLastName.getText(), tf_newUsername.getText(), tf_newPassword.getText());
                } else {
                    System.out.println("Please fill in all the information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to edit your profile!");
                    alert.show();
                }
            }
        });

        button_backToDashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "logged-in.fxml", "Log in!", null, null, null);
            }
        });

    }
}

