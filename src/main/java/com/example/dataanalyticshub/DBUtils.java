package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;

import static org.sqlite.SQLiteErrorCode.SQLITE_CONSTRAINT;

/* Do all the communications with the database
*  Reference: "JavaFX Login and Signup Form with Database Connection"
*  https://www.youtube.com/watch?v=ltX5AtW9v30*/

public class DBUtils {

    private static User currentUser;

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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Your account has been created successfully");
                alert.showAndWait();
                Navigator.changeScene(event, "logged-in.fxml", "Profile Updated");
                Navigator.changeScene(event, "logged-in.fxml", "Welcome!");
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
                        Navigator.changeScene(event, "logged-in.fxml","Welcome!");
                    } else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided password is incorrect");
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

            // Update the user's profile to database
            psUpdateProfile = connection.prepareStatement("UPDATE UserInfo SET firstName = ?, lastName = ?, username = ?, password = ? WHERE username = ?");
            psUpdateProfile.setString(1, newFirstName);
            psUpdateProfile.setString(2, newLastName);
            psUpdateProfile.setString(3, newUsername);
            psUpdateProfile.setString(4, newPassword);
            psUpdateProfile.setString(5, currentUser.getUsername());
            int rowsAffected = psUpdateProfile.executeUpdate();

            // if the new info successfully update to database
            if (rowsAffected > 0) {
                currentUser = new User(newUsername, newFirstName, newLastName, newPassword);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Changes have been saved");
                alert.showAndWait();
                Navigator.changeScene(event, "logged-in.fxml", "Profile Updated");
            } else {
                System.out.println("Failed to update profile");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Failed to update profile. Please try again.");
                alert.show();
            }
        } catch (SQLException e){
            // if the username already exist in the database
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

    public static void addPost(ActionEvent event, String content, String likesStr, String sharesStr, String date) {
        // Validate the input data
        ValidateUserInput validateUserInput = new ValidateUserInput();
        if (validateUserInput.hasComma(content) || !validateUserInput.isPositiveInteger(likesStr) || !validateUserInput.isPositiveInteger(sharesStr) || !validateUserInput.validateDateFromUser(date)) {
            System.out.println("Invalid post data. Please check your input.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid post data. Please check your input.");
            alert.show();
            return;
        }

        int likes = Integer.parseInt(likesStr);
        int shares = Integer.parseInt(sharesStr);

        // Insert the post into the database
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // Add a post to database
            psInsert = connection.prepareStatement("INSERT INTO Posts (content, likes, shares, date, userid) VALUES (?, ?, ?, ?, (SELECT userId FROM UserInfo WHERE username = ?))");
            psInsert.setString(1, content);
            psInsert.setInt(2, likes);
            psInsert.setInt(3, shares);
            psInsert.setString(4, date);
            psInsert.setString(5, currentUser.getUsername());

            int rowsAffected = psInsert.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post added successfully!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Post added successfully!");
                alert.showAndWait();
                Navigator.changeScene(event, "logged-in.fxml", "Post has been added");
            } else {
                System.out.println("Failed to add the post.");
                // Handle the case where the post was not added to the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (psInsert != null) {
                try {
                    psInsert.close();
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
