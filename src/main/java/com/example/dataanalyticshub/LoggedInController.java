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
    private Label label_welcome;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Go back to the first page when user click logout button
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "first-page.fxml", "Log in", null, null, null);

            }
        });

        button_editProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "edit-profile.fxml", "Edit Profile", null, null, null);
            }
        });

        button_addPost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "add-post.fxml", "Add a Post", null, null, null);
            }
        });

    }

    public void setUserInformation (String username, String firstname, String lastname){
        // Pass the old username when creating an instance of EditProfileController
//        EditProfileController editProfileController = new EditProfileController(username);
        label_welcome.setText("Welcome " + firstname + " " +lastname + "!");
    }


}
