package com.rishiprojects.attendancemanagement.admin_files.faculty;

public class Teacher {

    public String teacherName, teacherEmail, teacherUsername, teacherImage, adminId;

    public Teacher() {
    }

    public Teacher(String teacherName, String teacherEmail, String teacherUsername, String teacherImage, String adminID) {
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherUsername = teacherUsername;
        this.teacherImage = teacherImage;
        this.adminId = adminID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public String getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage) {
        this.teacherImage = teacherImage;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}