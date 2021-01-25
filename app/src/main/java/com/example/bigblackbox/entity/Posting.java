package com.example.bigblackbox.entity;

public class Posting {
    private int postID;             //帖子编号
    private String name;            //发帖人用户名
    private String title;           //帖子标题
    private String content;         //帖子内容
    private String time;            //发帖时间
    private int follow;             //帖子板块从属

    public Posting(){

    }

    public Posting(int postID, String name, String title, String content, String time, int follow) {
        this.postID = postID;
        this.name = name;
        this.title = title;
        this.content = content;
        this.time = time;
        this.follow = follow;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }
}
