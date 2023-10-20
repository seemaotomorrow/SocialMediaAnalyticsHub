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
        tf_content.setPromptText("Content cannot contain comma");
        tf_likes.setPromptText("Please provided a positive int");
        tf_shares.setPromptText("Please provided a positive int");
        tf_date.setPromptText("dd/MM/yyyy HH:mm");
        button_addPost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if the input are not empty
                String content = tf_content.getText();
                String likes = tf_likes.getText();
                String shares = tf_shares.getText();
                String date = tf_date.getText();
                // check if user input all the form
                if (!content.trim().isEmpty() && !likes.trim().isEmpty() && !shares.trim().isEmpty() && !date.trim().isEmpty()) {
                    ValidateUserInput validator = new ValidateUserInput();
                    // validate user input
                    if (!validator.hasComma(content) && validator.isPositiveInteger(likes) && validator.isPositiveInteger(shares) && validator.validateDateFromUser(date)) {
                        boolean addedSuccessfully = DBUtils.addPost(tf_content.getText(), tf_likes.getText(), tf_shares.getText(), tf_date.getText());
                        if (addedSuccessfully){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setContentText("Post been added successfully.");
                            alert.showAndWait();
                            Navigator.changeScene(event, "logged-in.fxml", "Post has been added");
                        } else {
                            System.out.println("Fail to add a post");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Fail to add a post");
                            alert.show();
                        }
                    } else {
                        System.out.println("Fail to add a post");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided information is not correct");
                        alert.show();

                    }
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
