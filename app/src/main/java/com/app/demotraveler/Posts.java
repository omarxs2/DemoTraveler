package com.app.demotraveler;




public class Posts {
        private String uId, Time, Date, PostImage, Description, UserProfileImage, FullName;

        public Posts()
        {

        }

    public Posts(String uId, String time, String date, String postImage, String description, String userProfileImage, String fullName) {
        this.uId = uId;
        Time = time;
        Date = date;
        PostImage = postImage;
        Description = description;
        UserProfileImage = userProfileImage;
        FullName = fullName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserProfileImage() {
        return UserProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        UserProfileImage = userProfileImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}

