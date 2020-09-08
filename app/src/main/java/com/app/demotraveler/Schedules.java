package com.app.demotraveler;

public class Schedules
{
    private String uId, Time, Date, ProfileImage, FullName,Location,Cost,Title,Tags;

    public Schedules()
    {

    }

    public Schedules(String uId, String time, String date, String profileImage, String fullName, String location, String cost, String title, String tags) {
        this.uId = uId;
        Time = time;
        Date = date;
        ProfileImage = profileImage;
        FullName = fullName;
        Location = location;
        Cost = cost;
        Title = title;
        Tags = tags;
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

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }
}
