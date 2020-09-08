package com.app.demotraveler;

public class Comments {

    private String ProfileImage,Date,Time,Comment,UserName,FullName,UId;

    public Comments() {
    }

    public Comments(String profileImage, String date, String time, String comment, String userName, String fullName, String UId) {
        ProfileImage = profileImage;
        Date = date;
        Time = time;
        Comment = comment;
        UserName = userName;
        FullName = fullName;
        this.UId = UId;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getComment() {
        return Comment;
    }

    public String getUserName() {
        return UserName;
    }

    public String getFullName() {
        return FullName;
    }

    public String getUId() {
        return UId;
    }
}
