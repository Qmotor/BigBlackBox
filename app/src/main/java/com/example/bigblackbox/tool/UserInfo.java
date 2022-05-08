package com.example.bigblackbox.tool;

public class UserInfo {
    /*
    UserInfo类为系统内存放静态变量的位置，旨在为更快速和方便实现某些功能
     */

    public static String getUserName() {
        return userName;
    }
    public static String getUserID(){
        return userID;
    }
    public static String getAdmin(){
        return isAdmin;
    }
    public static String getLord(){
        return isLord;
    }

    public static String userName = null;       // 当前登录用户——用户名

    public static String userID;                // 当前登录用户——用户ID

    public static String isAdmin;               // 当前登录用户——管理员身份

    public static String isLord = null;         // 当前登录用户——发帖人身份

    public static boolean userIcon = false;     // 当前登录用户——头像

    public static String jug = "全部";           // 专业课分区

    public static String regUName = null;       // 当前注册用户——用户名

    public static String regUPwd = null;        // 当前注册用户——密码

    public static boolean flag = true;

    public static String gender;

}
