package com.example.bigblackbox.entity;

public class Reply {
    private int replyID;                       //评论编号
    private int postID;                        //评论的帖子编号
    private String replyName;                  //发布评论的用户名
    private String replyContent;               //评论内容
    private String replyTime;                  //评论时间

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


