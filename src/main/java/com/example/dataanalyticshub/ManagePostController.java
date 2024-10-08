package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sqlite.core.DB;

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

    private final ValidateUserInput validator = new ValidateUserInput();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPostID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colLikes.setCellValueFactory(new PropertyValueFactory<>("likes"));
        colShares.setCellValueFactory(new PropertyValueFactory<>("shares"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));


        button_retrieve.setOnAction(event -> {retrievePostById();});
        button_export.setOnAction(this::exportButtonClicked);

        button_backToDashboard.setOnAction(event -> Navigator.changeScene(event, "logged-in.fxml", "Log in!"));

        button_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String postID = tf_postIdFromUser.getText();
                String username = DBUtils.getCurrentUser().getUsername();
                if (!postID.isEmpty()){
                    if (validator.isPositiveInteger(postID)){
                        boolean isRemoved = DBUtils.removePost(postID, username);
                        if (isRemoved){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setContentText("Post with ID " + postID + " has been successfully removed.");
                            alert.showAndWait();
                            // clear the input field and refresh the TableView
                            tf_postIdFromUser.clear();
                            tableView.getItems().clear();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("No post found with ID " + postID + ". Nothing was removed.");
                            alert.show();
                        }
                    } else {
                        System.out.println("Please provide a positive int");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please provide a positive int");
                        alert.show();
                    }
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
        User currentUser = DBUtils.getCurrentUser();
        String username = currentUser.getUsername(); // Get the currently logged-in user's username
        // check if user fill the form
        if (!postID.isEmpty()){
            // check if the provided id is a positive int
            if (validator.isPositiveInteger(postID)){
                Post retrievedPost = DBUtils.retrieveAPostByPostId(postID, username);
                if (retrievedPost != null) {
                    ObservableList<Post> data = FXCollections.observableArrayList(retrievedPost);
                    tableView.setItems(data);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Provided Post ID dose not exist");
                    alert.show();
                }
            } else {
                System.out.println("Please provide a positive int");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please provide a positive int");
                alert.show();
            }
        } else {
            System.out.println("Please provide a post ID");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please provide a post ID");
            alert.show();
        }
    }

    // current logged-in user can only see all of their own post
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


        if (!nFromUser.isEmpty() && validator.isPositiveInteger(nFromUser)){
            int nFromUserInt = Integer.parseInt(nFromUser);
            if (nFromUserInt != 0){
                User currentUser = DBUtils.getCurrentUser();
                String username = currentUser.getUsername(); // Get the currently logged-in user's username
                List<Post> allPosts = DBUtils.retrieveTopPostsByLikes(username,nFromUserInt);

                if (allPosts != null){
                    ObservableList<Post> data = FXCollections.observableArrayList(allPosts);
                    tableView.setItems(data);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No post in your collection yet");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please specify a number greater than zero");
                alert.show();
            }
        } else {
            System.out.print("Please specify the number of posts to retrieve (N): ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please specify the number of posts to retrieve (positive int)");
            alert.show();
        }
    }

    @FXML
    private void retrieveTopPostsFromEntireDatabaseByLikes() {
        String nFromUser = tf_nFromUser.getText();

        if (!nFromUser.isEmpty() && validator.isPositiveInteger(nFromUser)){
            User currentUser = DBUtils.getCurrentUser();
            String username = currentUser.getUsername(); // Get the currently logged-in user's username
            int n = Integer.parseInt(nFromUser);
            List<Post> allPosts = DBUtils.retrieveTopPostsFromEntireDatabaseByLikes(n);
            // check if there is no post in whole database
            if (allPosts != null){
                ObservableList<Post> data = FXCollections.observableArrayList(allPosts);
                tableView.setItems(data);
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No post in the whole database yet");
                alert.show();
            }

        } else {
            System.out.print("Please specify the number of posts to retrieve (N): ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please specify the number of posts to retrieve (positive int)");
            alert.show();
        }
    }
    @FXML
    private void exportButtonClicked(ActionEvent event) {
        String postId = tf_postIdFromUser.getText();
        User currentUser = DBUtils.getCurrentUser();
        String username = currentUser.getUsername();

        if (!postId.isEmpty() && validator.isPositiveInteger(postId)){
            Post retrievedPost = DBUtils.retrieveAPostByPostId(postId, username);
            if (retrievedPost != null){
                // if the post that user want to remove exists
                // use file chooser to implement this function
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Post as CSV");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                Node node = (Node) event.getSource();

                Stage stage = (Stage) node.getScene().getWindow();
                // Opening a dialog box and returns a file after click save
                // returns null if cancel
                File file = fileChooser.showSaveDialog(stage);

                if (file != null) {
                    // Extract folder path and filename from the chosen file
                    String folderPath = file.getParent();
                    String fileName = file.getName().replaceAll(".csv", "");

                    // Call the exportPostToCSV method from DBUtils
                    DBUtils.exportPostToCSV(postId,username, folderPath, fileName);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setContentText("Post with ID " + postId + " is exported successfully");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Post with ID " + postId + " is not found");
                alert.show();
            }
        } else {
            System.out.println("Please provide a post ID to export post ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please provide a post ID to export post (positive int)");
            alert.show();
        }
    }
}

