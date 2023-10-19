package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManagePostController implements Initializable {
    @FXML
    private Button button_retrieve;
    @FXML
    private Button button_remove;
    @FXML
    private Button button_showAllPosts;
    @FXML
    private Button button_topNPostsByLikes;
    @FXML
    private Button button_topNPostsByLikesInDB;
    @FXML
    private Button button_export;
    @FXML
    private Button button_backToDashboard;
    @FXML
    private TextField  tf_postIdFromUser;
    @FXML
    private TextField  tf_nFromUser;

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


        button_retrieve.setOnAction(event -> {retrievePostById();});
        button_export.setOnAction(event -> {retrievePostById();});

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

        button_showAllPosts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                retrievePostsByCurrentUser();
            }
        });

        button_topNPostsByLikes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                retrieveTopNPostsByLikes();
            }
        });
        button_topNPostsByLikesInDB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                retrieveTopPostsFromEntireDatabaseByLikes();
            }
        });
    }

    @FXML
    private void retrievePostById() {
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

    @FXML
    private void retrievePostsByCurrentUser() {
        User currentUser = DBUtils.getCurrentUser();
        String username = currentUser.getUsername(); // Get the currently logged-in user's username
        List<Post> allPosts = DBUtils.retrievePostsByCurrentUser(username);

        ObservableList<Post> data = FXCollections.observableArrayList(allPosts);
        tableView.setItems(data);
    }

    @FXML
    private void retrieveTopNPostsByLikes(){
        String nFromUser = tf_nFromUser.getText();

        if (!nFromUser.isEmpty()){
            User currentUser = DBUtils.getCurrentUser();
            String username = currentUser.getUsername(); // Get the currently logged-in user's username
            int n = Integer.parseInt(nFromUser);
            List<Post> allPosts = DBUtils.retrieveTopPostsByLikes(username,n);

            ObservableList<Post> data = FXCollections.observableArrayList(allPosts);
            tableView.setItems(data);
        } else {
            System.out.print("Please specify the number of posts to retrieve (N): ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please specify the number of posts to retrieve");
            alert.show();
        }
    }

    @FXML
    private void retrieveTopPostsFromEntireDatabaseByLikes() {
        String nFromUser = tf_nFromUser.getText();

        if (!nFromUser.isEmpty()){
            User currentUser = DBUtils.getCurrentUser();
            String username = currentUser.getUsername(); // Get the currently logged-in user's username
            int n = Integer.parseInt(nFromUser);
            List<Post> allPosts = DBUtils.retrieveTopPostsFromEntireDatabaseByLikes(n);

            ObservableList<Post> data = FXCollections.observableArrayList(allPosts);
            tableView.setItems(data);
        } else {
            System.out.print("Please specify the number of posts to retrieve (N): ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please specify the number of posts to retrieve");
            alert.show();
        }
    }

    @FXML
    private void exportPostToCSV (){
        String postID = tf_postIdFromUser.getText();
        if (!postID.isEmpty()){
            User currentUser = DBUtils.getCurrentUser();
            String username = currentUser.getUsername(); // Get the currently logged-in user's username
            // Prompt the user to choose a folder and file name for the export
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Post to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showSaveDialog(button_export.getScene().getWindow());

            if (selectedFile != null) {
                String exportFolderPath = selectedFile.getParent();
                String fileName = selectedFile.getName();

                // Call the DBUtils method to export the post to CSV
                boolean exportSuccess = DBUtils.exportPostToCSV(username, exportFolderPath, fileName);

                if (exportSuccess) {
                    // Show a success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Post exported to CSV successfully.");
                    alert.show();
                } else {
                    // Show an error message if the export failed
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Failed to export post to CSV.");
                    alert.show();
                }
            }
        } else {
            System.out.println("Please provide a post ID to export post");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please provide a post ID to export post");
            alert.show();
        }
    }
    }
