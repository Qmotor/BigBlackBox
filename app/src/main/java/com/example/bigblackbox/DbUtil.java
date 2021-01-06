package com.example.bigblackbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUtil extends SQLiteOpenHelper {

    public DbUtil(Context context){
        super(context,"BBB",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userInfo(userID integer primary key autoincrement,userName varchar(30),userPwd varchar(30),userGender char(10),userPhone char(11),userEmail varchar(30),userEdu varchar(30),userTargetSchol varchar(30),userCareer varchar(30))");
        db.execSQL("create table posting(postID integer primary key autoincrement,postUserName varchar(30),postTitle varchar(30),postContent varchar(50),postTime varchar(30),postFollow varchar(20))");
        db.execSQL("create table postReply(replyID integer primary key autoincrement,reply_postID integer,replyUserName varchar(30),replyContent varchar(30),replyTime varchar(30))");
        db.execSQL("create table pushPosting(pushPostingID integer primary key autoincrement,pushPostingUserName varchar(30),pushPostingTitle varchar(30),pushPostingContent varchar(50),pushPostingTime varchar(30))");
        db.execSQL("create table admin(adID integer primary key autoincrement,adName varchar(30),adPwd varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.print("版本更新至" + newVersion);
    }
}

