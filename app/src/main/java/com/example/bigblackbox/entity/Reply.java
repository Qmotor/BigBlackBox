package com.example.bigblackbox.entity;

public class Reply {
    private int replyID;                       // 评论编号
    private int postID;                        // 评论的帖子编号
    private String replyName;                  // 发布评论的用户名
    private String replyContent;               // 评论内容
    private String replyTime;                  // 评论时间
    private int pusherIdentity;                // 评论人身份
    private String replyUserGender;

    public Reply() {

    }

    public Reply(int replyID, int postID, String replyName, String replyContent, String replyTime, int pusherIdentity, String replyUserGender) {
        this.replyID = replyID;
        this.postID = postID;
        this.replyName = replyName;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
        this.pusherIdentity = pusherIdentity;
        this.replyUserGender = replyUserGender;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public void setPusherIdentity(int pusherIdentity) {
        this.pusherIdentity = pusherIdentity;
    }

    public String getReplyUserGender() {
        return replyUserGender;
    }

    public void setReplyUserGender(String replyUserGender) {
        this.replyUserGender = replyUserGender;
    }

    public int getReplyID() {
        return replyID;
    }

    public int getPostID() {
        return postID;
    }

    public String getReplyName() {
        return replyName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public int getPusherIdentity() {
        return pusherIdentity;
    }
}


