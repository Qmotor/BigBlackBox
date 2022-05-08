package com.example.bigblackbox.entity;

public class PushInfo {
    private int pushID;                      //推送消息编号
    private String pushUser;                 //发帖人用户名
    private String pushTitle;                //推送信息标题
    private String pushContent;              //推送信息内容
    private String pushTime;                 //发布时间
    private int pushFollow;                  //推送帖子版块从属
    private int pushIcon;

    public PushInfo() {

    }



    public PushInfo(int pushID, String pushUser, String pushTitle, String pushContent, String pushTime, int pushFollow, int pushIcon) {
        this.pushID = pushID;
        this.pushUser = pushUser;
        this.pushTitle = pushTitle;
        this.pushContent = pushContent;
        this.pushTime = pushTime;
        this.pushFollow = pushFollow;
        this.pushIcon = pushIcon;
    }

    public int getPushIcon() {
        return pushIcon;
    }

    public void setPushIcon(int pushIcon) {
        this.pushIcon = pushIcon;
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

    public int getPushFollow() {
        return pushFollow;
    }

    public void setPushFollow(int pushFollow) {
        this.pushFollow = pushFollow;
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

