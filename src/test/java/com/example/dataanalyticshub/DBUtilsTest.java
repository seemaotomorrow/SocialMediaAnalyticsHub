package com.example.dataanalyticshub;

import org.junit.jupiter.api.Test;
import org.sqlite.core.DB;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilsTest {

    @Test
    void signUpUser_validInfoSuccessful() {
        String firstName = "firstTest";
        String lastName = "Mao";
        String username = "testForSignUp";
        String password = "123m";
        boolean actualValue = false;
        // if user exists in database, delete user first
        if (DBUtils.logInUser(username, password)){
            DBUtils.deleteUser(username);
        }
        actualValue = DBUtils.signUpUser(firstName, lastName, username, password);

        assertTrue(actualValue);
    }

    @Test
    void logInUser_validInfo() {
        String username = "Mao";
        String password = "123m";

        boolean actualValue = DBUtils.logInUser(username, password);
        assertTrue(actualValue);

    }

    @Test
    void logInUser_invalidPassword() {
        String username = "Mao";
        String password = "invalid";

        boolean actualValue = DBUtils.logInUser(username, password);
        assertFalse(actualValue);
    }

    @Test
    void updateProfile_ChangeFirstName() {
        String username = "Mao";
        String password = "123m";
        DBUtils.logInUser(username, password);

        String newFirstName = "mia";
        String newLastName = "Mao";
        String newUsername = "Mao";
        String newPassword = "123m";


        boolean actualValue = DBUtils.updateProfile(newFirstName, newLastName, newUsername, newPassword);
        assertTrue(actualValue);
    }

    @Test
    void updateProfile_ChangeToAnExistingUsername() {
        String username = "Mao";
        String password = "123m";
        DBUtils.logInUser(username, password);

        String newFirstName = "new name";
        String newLastName = "Mao";
        String newUsername = "test1";
        String newPassword = "123m";

        boolean actualValue = DBUtils.updateProfile(newFirstName, newLastName, newUsername, newPassword);
        assertFalse(actualValue);
    }

    @Test
    void addPost_validPostInfo() {
        String content = "Doing test is fun";
        String likes = "1";
        String shares = "3";
        String date = "20/10/2023 10:20"; // (DD/MM/YYYY HH:MM)

        String username = "Mao";
        String password = "123m";
        DBUtils.logInUser(username, password);

        boolean  actualValue = DBUtils.addPost(content, likes, shares, date);
        assertTrue(actualValue);
    }

    @Test
    void addPost_InvalidPostInfo() {
        String content = "Doing test, is fun";
        String likes = "1";
        String shares = "3";
        String date = "20/10/2023 10:"; // (DD/MM/YYYY HH:MM)

        String username = "Mao";
        String password = "123m";
        DBUtils.logInUser(username, password);

        boolean  actualValue = DBUtils.addPost(content, likes, shares, date);
        assertFalse(actualValue);
    }


    @Test
    void upgradeToVIP_userIsNotVIP() {
        String username = "Mao";
        String password = "123m";
        DBUtils.logInUser(username, password);
        boolean actualValue = false;

        // if user is not vip
        if (Objects.equals(DBUtils.getCurrentUser().getIsVIP(), "No")) {
            actualValue = true;
        }

        assertTrue(actualValue);
    }


}
