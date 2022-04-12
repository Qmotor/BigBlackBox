package com.example.bigblackbox.entity;

public class User {
    private int userID;                     //用户ID
    private String userName;                //用户名
    private String userGender;              //性别
    private String userPhone;               //联系电话
    private String userEmail;               //电子邮箱
    private String userEdu;                 //教育经历
    private String userSchool;              //目标院校
    private String userCareer;              //当前职业
    private byte[] userIcon;


    public User(){

    }

    public User(int userID, String userName, String userGender, String userPhone, String userEmail, String userEdu, String userSchool, String userCareer, byte[] userIcon) {
        this.userID = userID;
        this.userName = userName;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userEdu = userEdu;
        this.userSchool = userSchool;
        this.userCareer = userCareer;
        this.userIcon = userIcon;
    }

    public int getUserID() {
        return userID;
    }

    public byte[] getUserIcon() {
        return userIcon;
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

