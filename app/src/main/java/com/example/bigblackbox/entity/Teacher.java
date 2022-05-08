package com.example.bigblackbox.entity;

public class Teacher {
    private int teacherID;              // 老师编号
    private String teacherName;         // 老师名字
    private String teacherIntro;        // 老师简介
    private byte[] teacherIcon;         // 老师头像
    private int teacherFollow;          // 科目分区
    private String proFollow;           // 专业课分区
    private String book1;               // 以下为代表书籍1~5
    private String book2;
    private String book3;
    private String book4;
    private String book5;

    public String getBook1() {
        return book1;
    }

    public void setBook1(String book1) {
        this.book1 = book1;
    }

    public String getBook2() {
        return book2;
    }

    public void setBook2(String book2) {
        this.book2 = book2;
    }

    public String getBook3() {
        return book3;
    }

    public void setBook3(String book3) {
        this.book3 = book3;
    }

    public String getBook4() {
        return book4;
    }

    public void setBook4(String book4) {
        this.book4 = book4;
    }

    public String getBook5() {
        return book5;
    }

    public String getProFollow() {
        return proFollow;
    }

    public void setProFollow(String proFollow) {
        this.proFollow = proFollow;
    }

    public void setBook5(String book5) {
        this.book5 = book5;
    }

    public Teacher(int teacherID, String teacherName, byte[] teacherIcon, int teacherFollow, String proFollow, String teacherIntro, String book1, String book2, String book3, String book4, String book5){
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.teacherIcon = teacherIcon;
        this.teacherIntro = teacherIntro;
        this.teacherFollow = teacherFollow;
        this.proFollow = proFollow;
        this.book1 = book1;
        this.book2 = book2;
        this.book3 = book3;
        this.book4 = book4;
        this.book5 = book5;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherIntro() {
        return teacherIntro;
    }

    public void setTeacherIntro(String teacherIntro) {
        this.teacherIntro = teacherIntro;
    }

    public byte[] getTeacherIcon() {
        return teacherIcon;
    }

    public void setTeacherIcon(byte[] teacherIcon) {
        this.teacherIcon = teacherIcon;
    }

    public int getTeacherFollow() {
        return teacherFollow;
    }

    public void setTeacherFollow(int teacherFollow) {
        this.teacherFollow = teacherFollow;
    }
}


