package com.example.bigblackbox.entity;

public class User {
    private int userID;                     //用户ID
    private int banned;                     //封禁状态
    private int admin;                      //管理员身份
    private String userName;                //用户名
    private String userGender;              //性别
    private String userPhone;               //联系电话
    private String userEmail;               //电子邮箱
    private String userEdu;                 //教育经历
    private String userSchool;              //目标院校
    private String userCareer;              //当前职业
    private byte[] userIcon;                //用户头像

    public User(){

    }

    public User(int userID,  String userName, int admin, int banned, String userGender, String userPhone, String userEmail, String userEdu, String userSchool, String userCareer, byte[] userIcon) {
        this.userID = userID;
        this.banned = banned;
        this.admin = admin;
        this.userName = userName;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userEdu = userEdu;
        this.userSchool = userSchool;
        this.userCareer = userCareer;
        this.userIcon = userIcon;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserEdu(String userEdu) {
        this.userEdu = userEdu;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public void setUserCareer(String userCareer) {
        this.userCareer = userCareer;
    }

    public void setUserIcon(byte[] userIcon) {
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

