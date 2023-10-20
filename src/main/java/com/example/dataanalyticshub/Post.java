package com.example.dataanalyticshub;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post {
    private int id;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime date; // (DD/MM/YYYY HH:MM)



    //constructor
    public Post(int id, String content, String author, int likes, int shares, String date){
        this.id = id;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.setDate(date);
    }


    // getter and setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    // getter and setter for likes
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    // getter and setter for shares
    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }


    // getter and setter for date
    public String getDate() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        // format the localDateTime into the wished string
        return date.format(outputFormatter);
    }

    public void setDate(String date) {
        // custom the date format
        // turn input String into DateTime
        this.date = DateUtils.parseDateString(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
