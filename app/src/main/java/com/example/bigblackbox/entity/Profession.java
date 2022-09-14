package com.example.bigblackbox.entity;

public class Profession {
    private int proID;             // 专业课信息编号
    private String proAuthor;      // 专业课信息上传者
    private String proTitle;       // 专业课信息标题
    private String proFirCont;     // 段落一
    private byte[] proFirPic;      // 图片一
    private String proSecCont;     // 段落二
    private byte[] proSecPic;      // 图片二
    private String proThrCont;     // 段落三
    private String proFollow;      // 专业课信息分类
    private byte[] proFace;        // 专业课信息封面

    public Profession(int proID, String proAuthor, String proTitle, String proFirCont,
                      byte[] proFirPic, String proSecCont,byte[] proSecPic,String proThrCont,
                      String proFollow, byte[] proFace){
        this.proID = proID;
        this.proAuthor = proAuthor;
        this.proTitle = proTitle;
        this.proFirCont = proFirCont;
        this.proFirPic = proFirPic;
        this.proSecCont = proSecCont;
        this.proSecPic = proSecPic;
        this.proThrCont = proThrCont;
        this.proFollow = proFollow;
        this.proFace = proFace;
    }

    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public String getProAuthor() {
        return proAuthor;
    }

    public void setProAuthor(String proAuthor) {
        this.proAuthor = proAuthor;
    }

    public String getProTitle() {
        return proTitle;
    }

    public void setProTitle(String proTitle) {
        this.proTitle = proTitle;
    }

    public String getProFirCont() {
        return proFirCont;
    }

    public void setProFirCont(String proFirCont) {
        this.proFirCont = proFirCont;
    }

    public byte[] getProFirPic() {
        return proFirPic;
    }

    public void setProFirPic(byte[] proFirPic) {
        this.proFirPic = proFirPic;
    }

    public String getProSecCont() {
        return proSecCont;
    }

    public void setProSecCont(String proSecCont) {
        this.proSecCont = proSecCont;
    }

    public byte[] getProSecPic() {
        return proSecPic;
    }

    public void setProSecPic(byte[] proSecPic) {
        this.proSecPic = proSecPic;
    }

    public String getProThrCont() {
        return proThrCont;
    }

    public void setProThrCont(String proThrCont) {
        this.proThrCont = proThrCont;
    }

    public String getProFollow() {
        return proFollow;
    }

    public void setProFollow(String proFollow) {
        this.proFollow = proFollow;
    }

    public byte[] getProFace() {
        return proFace;
    }

    public void setProFace(byte[] proFace) {
        this.proFace = proFace;
    }
}
