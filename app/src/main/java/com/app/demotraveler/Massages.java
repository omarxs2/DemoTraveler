package com.app.demotraveler;

public class Massages {

    private String Date,Massage,Sender,Time,Type;


    public Massages()
    {

    }


    public Massages(String date, String massage, String sender, String time, String type) {
        Date = date;
        Massage = massage;
        Sender = sender;
        Time = time;
        Type = type;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMassage() {
        return Massage;
    }

    public void setMassage(String massage) {
        Massage = massage;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
