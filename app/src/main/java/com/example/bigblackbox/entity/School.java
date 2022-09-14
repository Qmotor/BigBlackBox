package com.example.bigblackbox.entity;

public class School {
    private int schoolID;            // 院校编号
    private String schoolName;       // 院校名称
    private String schoolUrl;        // 院校官网链接
    private String firstCharacter;   // 院校首字母

    public School(Integer schoolID, String schoolName, String schoolUrl, String firstCharacter) {
        this.schoolID = schoolID;
        this.schoolName = schoolName;
        this.schoolUrl = schoolUrl;
        this.firstCharacter = firstCharacter;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public String getSchoolUrl() {
        return schoolUrl;
    }

    public void setSchoolUrl(String schoolUrl) {
        this.schoolUrl = schoolUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }
}
