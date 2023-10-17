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

public class AddPostController implements Initializable {
    @FXML
    private Button button_addPost;
    @FXML
    private Button button_backToDashboard;
    @FXML
    private TextField tf_content;
    @FXML
    private TextField tf_likes;
    @FXML
    private TextField tf_shares;
    @FXML
    private TextField tf_date;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        button_addPost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if the input are not empty
                if (!tf_content.getText().trim().isEmpty() && !tf_likes.getText().trim().isEmpty() && !tf_shares.getText().trim().isEmpty() && !tf_date.getText().trim().isEmpty()) {
                    DBUtils.addPost(event, tf_content.getText(), tf_likes.getText(), tf_shares.getText(), tf_date.getText());
                } else {
                    System.out.println("Please fill in all the information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to add a post!");
                    alert.show();
                }

            }
        });

        button_backToDashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Navigator.changeScene(event, "logged-in.fxml", "Log in!");
            }
        });
    }
}
