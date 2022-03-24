package com.example.bigblackbox;

public class UserInfo {

    public static String getUserName() {
        return userName;
    }
    public static String getUserID(){
        return userID;
    }
    public static String getAdmin(){
        return isAdmin;
    }
    public static boolean getLord(){
        return isLord;
    }

    public static String userName = null;

    public static String userID;

    public static String isAdmin;

    public static boolean isLord = false;
}
