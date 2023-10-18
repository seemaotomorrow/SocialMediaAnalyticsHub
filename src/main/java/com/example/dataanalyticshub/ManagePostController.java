package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagePostController implements Initializable {
    @FXML
    private Button button_retrieve;
    @FXML
    private Button button_remove;
    @FXML
    private Button button_backToDashboard;
    @FXML
    private TextField  tf_postIdFromUser;

    @FXML
    private TableView<Post> tableView;
    @FXML
    private TableColumn<Post, String> colPostID;
    @FXML
    private TableColumn<Post, String> colContent;
    @FXML
    private TableColumn<Post, String> colAuthor;
    @FXML
    private TableColumn<Post, String> colLikes;
    @FXML
    private TableColumn<Post, String> colShares;
    @FXML
    private TableColumn<Post, String> colDate;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPostID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colLikes.setCellValueFactory(new PropertyValueFactory<>("likes"));
        colShares.setCellValueFactory(new PropertyValueFactory<>("shares"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));


        button_retrieve.setOnAction(event -> retrievePost());

        button_backToDashboard.setOnAction(event -> Navigator.changeScene(event, "logged-in.fxml", "Log in!"));

        button_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String postID = tf_postIdFromUser.getText();
                if (!postID.isEmpty()){
                    DBUtils.removePost(postID);
                    // clear the input field and refresh the TableView
                    tf_postIdFromUser.clear();
                    tableView.getItems().clear();
                } else {
                    System.out.println("Please provide a post ID to remove!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please provide a post ID to remove!");
                    alert.show();
                }
            }
        });
    }

    @FXML
    private void retrievePost() {
        String postID = tf_postIdFromUser.getText();
        if (!postID.isEmpty()){
            Post retrievedPost = DBUtils.retrievePost(postID);
            ObservableList<Post> data = FXCollections.observableArrayList(retrievedPost);
            tableView.setItems(data);
        } else {
            System.out.println("Please provide a post ID");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please provide a post ID");
            alert.show();
        }
    }
}
