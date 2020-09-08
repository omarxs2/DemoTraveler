package com.app.demotraveler;

public class FindFriends {

    private String ProfileImage,Bio,UserName,FullName;

    public FindFriends(){

    }

    public FindFriends(String profileImage, String bio, String userName, String fullName) {
        ProfileImage = profileImage;
        Bio = bio;
        UserName = userName;
        FullName = fullName;
    }


    public String getProfileImage() {
        return ProfileImage;
    }

    public String getBio() {
        return Bio;
    }

    public String getUserName() {
        return UserName;
    }

    public String getFullName() {
        return FullName;
    }
}
