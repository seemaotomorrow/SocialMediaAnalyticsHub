package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.sqlite.SQLiteErrorCode.SQLITE_CONSTRAINT;

/* Do all the communications with the database
*  Reference: "JavaFX Login and Signup Form with Database Connection"
*  https://www.youtube.com/watch?v=ltX5AtW9v30*/

public class DBUtils {

    private static User currentUser;

    public static boolean signUpUser(String firstName, String lastName, String username, String password){

        Connection connection = null;
        // use for query the database
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        // contain the data return from the database when query it
        ResultSet resultSet = null;


        try
        {
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
                psInsert  =connection.prepareStatement("INSERT INTO UserInfo (firstName, lastName, username, password, isVIP) VALUES(?, ?, ?, ?, ?)");
                psInsert.setString(1,firstName);
                psInsert.setString(2,lastName);
                psInsert.setString(3,username);
                psInsert.setString(4,password);
                psInsert.setString(5,"No");
                int rowsAffected = psInsert.executeUpdate();

                // if the new info successfully update to database
                if (rowsAffected > 0) {
                    System.out.println("Signed up successfully");
                    return true;
                } else {
                    System.out.println("Failed to sign up");
                    return false;
                }
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
        return false;
    }


    public static boolean logInUser (String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("SELECT password, firstName, lastName, isVIP FROM UserInfo WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            // if user is not found in the database, prompt user that
            if (!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database");
                return false;
            } else {
                // when user exists in the database
                while (resultSet.next()){
                    // retrieve password, firstName, lastName from database
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedFirstName = resultSet.getString("firstName");
                    String retrievedLastName = resultSet.getString("lastName");
                    String retrievedUserType = resultSet.getString("isVIP");
                    // if the password user input matches the database, show logged-in page
                    if (retrievedPassword.equals(password)){
                        // renew current user information for later information retrieve
                        currentUser = new User(username, retrievedFirstName, retrievedLastName, retrievedPassword, retrievedUserType);
                        return true;
                    } else {
                        System.out.println("Password did not match");
                        return false;
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
        return false;
    }

    public static boolean updateProfile(String newFirstName, String newLastName, String newUsername, String newPassword){
        Connection connection = null;
        // use for query the database
        PreparedStatement psUpdateProfile = null;
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
                currentUser = new User(newUsername, newFirstName, newLastName, newPassword, currentUser.getIsVIP());
                System.out.println("User info updated");
                return true;
            } else {
                System.out.println("Failed to update profile");
                return false;
            }
        } catch (SQLException e){
            // if the username already exist in the database
            if (e.getErrorCode() == SQLITE_CONSTRAINT.code) {
                System.out.println("Username is already in use");
                return false;
            } else {
                e.printStackTrace();
            }
        }finally {
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
        return false;
    }

    public static boolean addPost(String content, String likesStr, String sharesStr, String date) {
        int likesInt = Integer.parseInt(likesStr);
        int sharesInt = Integer.parseInt(sharesStr);
        // Insert the post into the database
        Connection connection = null;
        PreparedStatement psInsert = null;

        ValidateUserInput validator = new ValidateUserInput();
        // validate user input
        if (!validator.hasComma(content) && validator.isPositiveInteger(likesStr) && validator.isPositiveInteger(sharesStr) && validator.validateDateFromUser(date)) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
                // Add a post to database
                psInsert = connection.prepareStatement("INSERT INTO Posts (content, likes, shares, date, userid) VALUES (?, ?, ?, ?, (SELECT userId FROM UserInfo WHERE username = ?))");
                psInsert.setString(1, content);
                psInsert.setInt(2, likesInt);
                psInsert.setInt(3, sharesInt);
                psInsert.setString(4, date);
                psInsert.setString(5, currentUser.getUsername());

                int rowsAffected = psInsert.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Post added successfully!");
                    return true;
                } else {
                    System.out.println("Failed to add the post.");
                    return false;
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
        return false;
    }

    public static Post retrieveAPostByPostId(String postID, String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Create a Post object to store retrieved details
        Post post = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("SELECT * FROM posts WHERE postId = ? AND userId = (SELECT userId FROM UserInfo WHERE username = ?)");
            preparedStatement.setString(1, postID);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();

            // if the provided post ID doesn't exist
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Post ID is not found in the database");
            } else {
                // post ID exists
                // Retrieve post details from the database
                int id = resultSet.getInt("postId");
                String content = resultSet.getString("content");
                int likes = resultSet.getInt("likes");
                int shares = resultSet.getInt("shares");
                String date = resultSet.getString("date");
                String author = DBUtils.getCurrentUser().getUsername();

                post = new Post(id, content,author, likes, shares, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
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
        return post; // Return null if the post is not found
    }

    public static boolean removePost(String postId, String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("DELETE FROM posts WHERE postId = ? AND userId = (SELECT userId FROM UserInfo WHERE username = ?)");
            preparedStatement.setString(1, postId);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Post with ID " + postId + " has been successfully removed.");
                return true;

            } else {
                System.out.println("No post found with ID " + postId + ". Nothing was removed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close all the resources
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
        return false;
    }

    public static List<Post> retrievePostsByCurrentUser(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Post> userPosts = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            String sql = "SELECT * FROM posts WHERE userId = (SELECT userId FROM UserInfo WHERE username = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                // Retrieve post data and create Post objects
                int id = resultSet.getInt("postId");
                String content = resultSet.getString("content");
                int likes = resultSet.getInt("likes");
                int shares = resultSet.getInt("shares");
                String date = resultSet.getString("date");
                String author = DBUtils.getCurrentUser().getUsername();

                // Create a Post object and add it to the list
                Post post = new Post(id, content, author, likes, shares, date);
                userPosts.add(post);
            }

            if (userPosts.isEmpty()) {
                System.out.println("No post in your collection, please add a post first!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No post in your collection, please add a post first!");
                alert.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
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

        return userPosts;
    }

    public static List<Post> retrieveTopPostsByLikes(String username, int numberOfPosts) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Post> topPosts = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // check if the numberOfPosts user want to retrieve > posts in the database
            String countQuery = "SELECT COUNT(*) FROM posts WHERE userId = (SELECT userId FROM UserInfo WHERE username = ?)";
            preparedStatement = connection.prepareStatement(countQuery);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int totalPosts = resultSet.getInt(1);
            // if user is not found in the database, prompt user that
            if (totalPosts == 0){
                System.out.println("No post in the database yet");
                return null;
            }

            if (numberOfPosts > totalPosts) {
                // User requested more posts than available
                System.out.println("Only " + totalPosts + " posts exist in the collection. Showing all of them.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Only " + totalPosts + " posts exist in the collection. Showing all of them.");
                alert.showAndWait();
                numberOfPosts = totalPosts;
            }

            String query = "SELECT * FROM posts WHERE userId = (SELECT userId FROM UserInfo WHERE username = ?) ORDER BY likes DESC LIMIT ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, numberOfPosts);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Retrieve post data and create Post objects
                int id = resultSet.getInt("postId");
                String content = resultSet.getString("content");
                int likes = resultSet.getInt("likes");
                int shares = resultSet.getInt("shares");
                String date = resultSet.getString("date");
                String author = DBUtils.getCurrentUser().getUsername();

                Post post = new Post(id, content, author, likes, shares, date);
                topPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
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

        if (topPosts.isEmpty()) {
            System.out.println("No post in your collection, please add a post first!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No post in your collection, please add a post first!");
            alert.show();
        }

        return topPosts;
    }

    public static List<Post> retrieveTopPostsFromEntireDatabaseByLikes(int numberOfPosts) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Post> topPosts = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // Check if the numberOfPosts user wants to retrieve is greater than the total posts in the database
            String countQuery = "SELECT COUNT(*) FROM posts";
            preparedStatement = connection.prepareStatement(countQuery);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int totalPosts = resultSet.getInt(1);

            if (numberOfPosts > totalPosts) {
                // User requested more posts than available
                System.out.println("Only " + totalPosts + " posts exist in the collection. Showing all of them.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Only " + totalPosts + " posts exist in the collection. Showing all of them.");
                alert.showAndWait();
                numberOfPosts = totalPosts;
            }

            String query = """
                SELECT posts.*, userInfo.username AS author
                FROM posts
                JOIN (
                    SELECT userId, username
                    FROM userInfo
                ) AS userInfo ON posts.userId = userInfo.userId
                ORDER BY likes DESC
                LIMIT ?""";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, numberOfPosts);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Retrieve post data and create Post objects
                int id = resultSet.getInt("postId");
                String content = resultSet.getString("content");
                int likes = resultSet.getInt("likes");
                int shares = resultSet.getInt("shares");
                String date = resultSet.getString("date");
                String author = resultSet.getString("author");

                Post post = new Post(id, content, author, likes, shares, date);
                topPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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

        if (topPosts.isEmpty()) {
            System.out.println("No posts found in the database.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("No posts found in the database.");
            alert.showAndWait();
        }
        return topPosts;
    }

    public static void exportPostToCSV(String postID, String username, String exportFolderPath, String fileName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("SELECT * FROM posts WHERE postId = ? AND userId = (SELECT userId FROM UserInfo WHERE username = ?)");
            preparedStatement.setString(1, postID);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve post data and create Post objects
                int id = resultSet.getInt("postId");
                String content = resultSet.getString("content");
                int likes = resultSet.getInt("likes");
                int shares = resultSet.getInt("shares");
                String date = resultSet.getString("date");
                String author = DBUtils.getCurrentUser().getUsername();

                // Construct the CSV data
                String csvData = id + "," + content + "," + author + "," + likes + "," + shares + "," + date;

                // Create the file path and check if it exists
                File exportFolder = new File(exportFolderPath);
                if (!exportFolder.exists()) {
                    exportFolder.mkdirs();
                }

                // Create the file and write CSV data
                File exportFile = new File(exportFolder, fileName);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
                    writer.write(csvData);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Post exported successfully.");
            } else {
                System.out.println("Post not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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

    public static boolean upgradeToVIP (String username) {
        Connection connection = null;
        PreparedStatement psUpdateToVIP = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            psUpdateToVIP = connection.prepareStatement("UPDATE UserInfo SET isVIP = ? WHERE username = ?");
            psUpdateToVIP.setString(1, "Yes");
            psUpdateToVIP.setString(2, username);

            int rowsAffected = psUpdateToVIP.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Upgrade to VIP successfully.\n Please log out and log in again to access VIP functionalities.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Upgrade to VIP successfully.\n Please log out and log in again to access VIP functionalities.");
                ButtonType logOutButton = new ButtonType("Log Out");
                alert.getButtonTypes().setAll(logOutButton);
                // Show the dialog and wait for the user's choice
                Optional<ButtonType> userChoice = alert.showAndWait();

                if (userChoice.isPresent() && userChoice.get() == logOutButton) {
                    return true;
                }
            } else {
                System.out.println("Failed upgrade to VIP.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (psUpdateToVIP, connection)
            if (psUpdateToVIP != null) {
                try {
                    psUpdateToVIP.close();
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
        return false;
    }

    public static CategoryShareCounts getShareCategoryCounts(String username) {
        Connection connection = null;
        PreparedStatement psSelectCategorizedShares = null;
        ResultSet resultSet = null;
        // create a CategoryShareCounts instance to store the result
        CategoryShareCounts shareCounts = new CategoryShareCounts();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");

            // Create a SQL query to count shares in each category
            String query = "SELECT " +
                "SUM(CASE WHEN shares >= 0 AND shares <= 99 THEN 1 ELSE 0 END) as category0_99, " +
                "SUM(CASE WHEN shares >= 100 AND shares <= 999 THEN 1 ELSE 0 END) as category100_999, " +
                "SUM(CASE WHEN shares >= 1000 THEN 1 ELSE 0 END) as category1000_plus " +
                "FROM posts WHERE userId = (SELECT userId FROM UserInfo WHERE username = ?)";

            psSelectCategorizedShares = connection.prepareStatement(query);
            psSelectCategorizedShares.setString(1, username);

            resultSet = psSelectCategorizedShares.executeQuery();

            if (resultSet.next()) {
                shareCounts.setCategory0_99(resultSet.getInt("category0_99"));
                shareCounts.setCategory100_999(resultSet.getInt("category100_999"));
                shareCounts.setCategory1000_plus(resultSet.getInt("category1000_plus"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (resultSet, preparedStatement, connection)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psSelectCategorizedShares != null) {
                try {
                    psSelectCategorizedShares.close();
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
        return shareCounts;
    }

    public static boolean bulkImportFromCSV(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // Read the CSV file row by row
            String line;
            boolean firstLine = true;
            // Read each line of the CSV file
            while ((line = br.readLine()) != null) {
                // Split the line into columns using a comma as the delimiter
                String[] columns = line.split(",");
                // skip the header in csv file
                if (firstLine){
                    firstLine = false;
                    continue;
                }

                if (columns.length == 6) {
                    String content = columns[1];
                    String likes = columns[3];
                    String shares = columns[4];
                    String dateTime = columns[5];
                    addPost(content, likes, shares, dateTime);
                } else {
                    // Handle invalid data format (e.g., log, throw an exception, or skip)
                    System.out.println("The csv file does not follow the correct format");
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // remove a user from db
    public static boolean deleteUser (String username){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:userInfo.db");
            preparedStatement = connection.prepareStatement("DELETE FROM UserInfo WHERE username = ?");
            preparedStatement.setString(1, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User has been deleted");
                return true;

            } else {
                System.out.println("fail to delete user");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close all the resources
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
        return false;
    }


    public static User getCurrentUser() {
        return currentUser;
    }

}
