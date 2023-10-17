package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static org.sqlite.SQLiteErrorCode.SQLITE_CONSTRAINT;
import static org.sqlite.SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE;

/* Do all the communications with the database
*  Reference: "JavaFX Login and Signup Form with Database Connection"
*  https://www.youtube.com/watch?v=ltX5AtW9v30*/

public class DBUtils {

    private static User currentUser;

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String firstname, String lastname){
        Parent root = null;

        if (username != null){
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username, firstname, lastname);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else{
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

    }

    public static void signUpUser(ActionEvent event, String firstName, String lastName, String username, String password){

        Connection connection = null;
        // use for query the database
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        // contain the data return from the database when query it
        ResultSet resultSet = null;


        try{
            // connect the database
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // Check if the username is already in use
            psCheckUserExists = connection.prepareStatement("SELECT * FROM UserInfo WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            // isBeforeFirst return false: resultSet is empty
            // isBeforeFirst return true: resultSet is not empty
            // if the resultSet is not empty, means the username already exist in the database
            if (resultSet.isBeforeFirst()){
                System.out.println("username already exit");
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username");
                alert.show();
            } else{
                psInsert  =connection.prepareStatement("INSERT INTO UserInfo (firstName, lastName, username, password) VALUES(?, ?, ?, ?)");
                psInsert.setString(1,firstName);
                psInsert.setString(2,lastName);
                psInsert.setString(3,username);
                psInsert.setString(4,password);
                psInsert.executeUpdate();
                changeScene(event, "logged-in.fxml", "Welcome!", username, firstName, lastName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally { // close all the database connection to avoid memory leakage
            if (resultSet != null){
                try{
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try {
                    psInsert.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public static void logInUser (ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("SELECT password, firstName, lastName FROM UserInfo WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            // if user is not found in the database, prompt user that
            if (!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                // when user exists in the database
                while (resultSet.next()){
                    // retrieve password, firstName, lastName from database
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedFirstName = resultSet.getString("firstName");
                    String retrievedLastName = resultSet.getString("lastName");
                    // if the password user input matches the database, show logged-in page
                    if (retrievedPassword.equals(password)){
                        currentUser = new User(username, retrievedFirstName, retrievedLastName, retrievedPassword);
                        changeScene(event, "logged-in.fxml","Welcome!", username, retrievedFirstName, retrievedLastName);
                    } else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {  // close all the database connection to avoid memory leakage
            if (resultSet != null){
                try{
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void updateProfile(ActionEvent event, String newFirstName, String newLastName, String newUsername, String newPassword){
        Connection connection = null;
        // use for query the database
        PreparedStatement psUpdateProfile = null;
        PreparedStatement psCheckUserExists = null;
        // contain the data return from the database when query it
        ResultSet resultSet = null;


        try {
            // connect the database
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // Check if the new username is already in use
//            psCheckUserExists = connection.prepareStatement("SELECT * FROM UserInfo WHERE username = ?");
//            psCheckUserExists.setString(1, newUsername);
//            resultSet = psCheckUserExists.executeQuery();
//
//            if (resultSet.isBeforeFirst()) {
//                System.out.println("Username is already in use");
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Username is already in use. Please choose a different username.");
//                alert.show();
//            } else
            {
                // Update the user's profile to database
                psUpdateProfile = connection.prepareStatement("UPDATE UserInfo SET firstName = ?, lastName = ?, username = ?, password = ? WHERE username = ?");
                psUpdateProfile.setString(1, newFirstName);
                psUpdateProfile.setString(2, newLastName);
                psUpdateProfile.setString(3, newUsername);
                psUpdateProfile.setString(4, newPassword);
                psUpdateProfile.setString(5, currentUser.getUsername());
                int rowsAffected = psUpdateProfile.executeUpdate();


                if (rowsAffected > 0) {
                    currentUser = new User(newUsername, newFirstName, newLastName, newPassword);
                    changeScene(event, "logged-in.fxml", "Profile Updated", newUsername, newFirstName, newLastName);
                } else {
                    System.out.println("Failed to update profile");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Failed to update profile. Please try again.");
                    alert.show();
                }
            }
        } catch (SQLException e){
            if (e.getErrorCode() == SQLITE_CONSTRAINT.code) {
                System.out.println("Username is already in use");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username is already in use. Please choose a different username.");
                alert.show();
            } else {
                e.printStackTrace();
            }
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psUpdateProfile != null) {
                try {
                    psUpdateProfile.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
