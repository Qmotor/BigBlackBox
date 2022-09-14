package com.example.bigblackbox.entity;

public class Music {
    private int musicID;            // 音频资料编号
    private String musicName;       // 音频资料名称
    private String musicAuthor;     // 音频资料作者
    private String musicUrl;        // 音频资料地址
    private int musicFollow;        // 音频资料学科分类

    public Music(int musicID, String musicName, String musicAuthor, String musicUrl, int musicFollow) {
        this.musicID = musicID;
        this.musicName = musicName;
        this.musicAuthor = musicAuthor;
        this.musicUrl = musicUrl;
        this.musicFollow = musicFollow;
    }

    public int getMusicID() {
        return musicID;
    }

    public void setMusicID(int musicID) {
        this.musicID = musicID;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public int getMusicFollow() {
        return musicFollow;
    }

    public void setMusicFollow(int musicFollow) {
        this.musicFollow = musicFollow;
    }
}
