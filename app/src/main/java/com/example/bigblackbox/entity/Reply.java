package com.example.bigblackbox.entity;

public class Reply {
    private int replyID;
    private int postID;
    private String replyName;
    private String replyContent;
    private String replyTime;

    public Reply() {

    }

    public Reply(int replyID, int postID, String replyName, String replyContent, String replyTime) {
        this.replyID = replyID;
        this.postID = postID;
        this.replyName = replyName;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
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
}


