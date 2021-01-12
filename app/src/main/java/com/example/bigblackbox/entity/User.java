package com.example.bigblackbox.entity;

public class User {
    private int userID;
    private String userName;
    private String userGender;

    public User(){

    }

    public User(int userID, String userName, String userGender) {
        this.userID = userID;
        this.userName = userName;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public int getUserID() {
        return userID;
    }
}

