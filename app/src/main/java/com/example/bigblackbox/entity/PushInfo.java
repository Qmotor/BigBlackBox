package com.example.bigblackbox.entity;

public class PushInfo {
    private int pushID;
    private String pushUser;
    private String pushTitle;
    private String pushContent;
    private String pushTime;

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

