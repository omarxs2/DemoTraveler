package com.app.demotraveler;

public class Days {


    private String Day,Activities,Food,Description;

    public Days() {
    }

    public Days(String day, String activities, String food, String description) {
        Day = day;
        Activities = activities;
        Food = food;
        Description = description;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getActivities() {
        return Activities;
    }

    public void setActivities(String activities) {
        Activities = activities;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
