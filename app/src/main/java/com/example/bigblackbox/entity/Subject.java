package com.example.bigblackbox.entity;

public class Subject {
    private int subID;
    private String subAuthor;
    private String subTitle;
    private String firCont;
    private byte[] firPic;
    private String secCont;
    private byte[] secPic;
    private String thrCont;
    private int subFollow;
    private int follChoose;

    public Subject(int subID, String subAuthor, String subTitle, String firCont, byte[] firPic, String secCont, byte[] secPic, String thrCont, int subFollow, int follChoose){
        this.subID = subID;
        this.subAuthor = subAuthor;
        this.subTitle = subTitle;
        this.firCont = firCont;
        this.firPic = firPic;
        this.secCont = secCont;
        this.secPic = secPic;
        this.thrCont = thrCont;
        this.subFollow = subFollow;
        this.follChoose = follChoose;
    }

    public int getFollChoose() {
        return follChoose;
    }

    public void setFollChoose(int follChoose) {
        this.follChoose = follChoose;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public String getSubAuthor() {
        return subAuthor;
    }

    public void setSubAuthor(String subAuthor) {
        this.subAuthor = subAuthor;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getFirCont() {
        return firCont;
    }

    public void setFirCont(String firCont) {
        this.firCont = firCont;
    }

    public byte[] getFirPic() {
        return firPic;
    }

    public void setFirPic(byte[] firPic) {
        this.firPic = firPic;
    }

    public String getSecCont() {
        return secCont;
    }

    public void setSecCont(String secCont) {
        this.secCont = secCont;
    }

    public byte[] getSecPic() {
        return secPic;
    }

    public void setSecPic(byte[] secPic) {
        this.secPic = secPic;
    }

    public String getThrCont() {
        return thrCont;
    }

    public void setThrCont(String thrCont) {
        this.thrCont = thrCont;
    }

    public int getSubFollow() {
        return subFollow;
    }

    public void setSubFollow(int subFollow) {
        this.subFollow = subFollow;
    }
}
