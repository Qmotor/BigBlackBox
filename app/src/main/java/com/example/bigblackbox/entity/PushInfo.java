package com.example.bigblackbox.entity;

public class PushInfo {
    private int pushID;                      //推送消息编号
    private String pushUser;                 //发帖人用户名
    private String pushTitle;                //推送信息标题
    private String pushContent;              //推送信息内容
    private String pushTime;                 //发布时间

    public PushInfo() {

    }

    public PushInfo(int pushID, String postUser, String postTitle, String postContent, String postTime) {
        this.pushID = pushID;
        this.pushUser = postUser;
        this.pushTitle = postTitle;
        this.pushContent = postContent;
        this.pushTime = postTime;
    }

    public int getPushID() {
        return pushID;
    }

    public void setPushID(int pushID) {
        this.pushID = pushID;
    }

    public String getPushUser() {
        return pushUser;
    }

    public void setPushUser(String pushUser) {
        this.pushUser = pushUser;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}

