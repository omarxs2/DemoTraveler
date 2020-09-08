package com.app.demotraveler;

public class Going {
   private String Date,Time,Date1,Date2,From,To,Notes,UserID;


    public Going(String date, String time, String date1, String date2, String from, String to, String notes, String UserID) {
        Date = date;
        Time = time;
        Date1 = date1;
        Date2 = date2;
        From = from;
        To = to;
        Notes = notes;
        UserID = UserID;
    }

    public Going(){

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUsetrID(String usetrID) {
        UserID = usetrID;
    }
}
