package com.example.bigblackbox.entity;

public class User {
    private int userID;
    private String userName;
    private String userGender;
    private String userPhone;
    private String userEmail;
    private String userEdu;
    private String userSchool;
    private String userCareer;


    public User(){

    }

    public User(int userID, String userName, String userGender, String userPhone, String userEmail, String userEdu, String userSchool, String userCareer) {
        this.userID = userID;
        this.userName = userName;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userEdu = userEdu;
        this.userSchool = userSchool;
        this.userCareer = userCareer;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserEdu() {
        return userEdu;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public String getUserCareer() {
        return userCareer;
    }
}

