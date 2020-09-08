package com.app.demotraveler;

public class Activities {


    private String Activity,Location,Place,Description,Type;

    public Activities() {
    }

    public Activities(String activity, String location, String place, String description, String type) {
        Activity = activity;
        Location = location;
        Place = place;
        Description = description;
        Type = type;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
