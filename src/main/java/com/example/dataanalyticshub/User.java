package com.example.dataanalyticshub;

public class User {
    public User(String username, String firstName, String lastName, String password, String isVIP) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isVIP = isVIP;
    }

    public String getIsVIP() {return isVIP; }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private static String isVIP;
}
