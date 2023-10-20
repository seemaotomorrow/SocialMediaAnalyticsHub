package com.example.dataanalyticshub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Optional;
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
    private Button button_upgradeToVIP;
    @FXML
    private Button button_dataVisualization;
    @FXML
    private Button button_importPostsFromCSV;
    @FXML
    private Label label_welcome;
    @FXML
    private Label label_youAreNotVIP;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = DBUtils.getCurrentUser();
        label_welcome.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");

        if (currentUser.getIsVIP().equals("Yes")){
            label_youAreNotVIP.setText("You are a VIP member");
            // Hide the upgrade ro VIP button if user already become vip
            button_upgradeToVIP.setVisible(false);

            // Show advanced functionalities if user is VIP
            button_dataVisualization.setVisible(true);
            button_importPostsFromCSV.setVisible(true);
        } else {
            label_youAreNotVIP.setText("You are not a VIP member yet");
            // Hide advanced functionalities if user is VIP
            button_dataVisualization.setVisible(false);
            button_importPostsFromCSV.setVisible(false);
        }

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
                Navigator.changeScene(event, "manage-post.fxml", "Manage Posts");
            }
        });

        button_upgradeToVIP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("VIP Upgrade Confirmation");
                confirmationAlert.setHeaderText("Upgrade to VIP");
                confirmationAlert.setContentText("Would you like to subscribe to the application for a monthly fee of $0?");

                // Customize the buttons for user choice
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                // Show the dialog and wait for the user's choice
                Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

                if (userChoice.isPresent() && userChoice.get() == yesButton) {
                    // The user agreed to upgrade to VIP
                    User currentUser = DBUtils.getCurrentUser();
                    String username = currentUser.getUsername();
                    boolean upgraded =  DBUtils.upgradeToVIP(username);
                    if (upgraded){
                        Navigator.changeScene(event, "first-page.fxml","Data Analytics Hub");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Failed to upgrade to VIP.");
                        alert.show();
                    }
                }
            }
        });

        button_dataVisualization.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generatePieChart(event);
            }
        });

        button_importPostsFromCSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                importPostsFromCSV(event);
            }
        });
        }

    @FXML
    private void generatePieChart(ActionEvent event) {
        String username = DBUtils.getCurrentUser().getUsername();
        CategoryShareCounts shareCounts = DBUtils.getShareCategoryCounts(username);

        int category1 = shareCounts.getCategory0_99(); // Count of shares from 0 to 99
        int category2 = shareCounts.getCategory100_999(); // Count of shares from 100 to 999
        int category3 = shareCounts.getCategory1000_plus(); // Count of shares equal or exceeding 1000
        // Create data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("0-99 Shares", category1),
            new PieChart.Data("100-999 Shares", category2),
            new PieChart.Data("1000+ Shares", category3)
        );

        // Create the pie chart
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Distribution of Shares");

        // Create a new stage for the pie chart and add it to the scene
        javafx.stage.Stage chartStage = new javafx.stage.Stage();
        chartStage.setTitle("Share Distribution Chart");
        chartStage.setScene(new Scene(chart));
        chartStage.show();
    }

    @FXML
    private void importPostsFromCSV(ActionEvent event) {
        // Create a FileChooser to allow the user to select a CSV file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Select a CSV file");

        // Show the file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Get the path of the selected CSV file
            String csvFilePath = selectedFile.getAbsolutePath();

            // Call the bulkImportFromCSV method from your DBUtils class
            boolean isImported = DBUtils.bulkImportFromCSV(csvFilePath);
            if (isImported){
                // Prompt user after all the posts been added successfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Posts from csv file been added successfully to your collection.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The csv file does not follow the correct format");
                alert.show();
            }
        }
    }
}
