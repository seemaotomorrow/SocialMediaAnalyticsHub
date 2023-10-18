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
    private Button button_editProfile;
    @FXML
    private Button button_addPost;
    @FXML
    private Button button_managePost;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = DBUtils.getCurrentUser();
        label_welcome.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");

        // Go back to the first page when user click logout button
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "first-page.fxml", "Log in");

            }
        });

        button_editProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "edit-profile.fxml", "Edit Profile");
            }
        });

        button_addPost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "add-post.fxml", "Add a Post");
            }
        });

        button_managePost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "manage-post.fxml", "Retrieve a Post");
            }
        });

    }


}
