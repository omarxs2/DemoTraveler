package com.app.demotraveler;

public class Offers {
    private String EndingDate,PeopleNo,StartingDate,SenderID,Notes,RequestStatus;

    public Offers(){

    }

    public Offers(String endingDate, String peopleNo, String startingDate, String senderID, String notes, String requestStatus) {
        EndingDate = endingDate;
        PeopleNo = peopleNo;
        StartingDate = startingDate;
        SenderID = senderID;
        Notes = notes;
        RequestStatus = requestStatus;
    }

    public String getEndingDate() {
        return EndingDate;
    }

    public void setEndingDate(String endingDate) {
        EndingDate = endingDate;
    }

    public String getPeopleNo() {
        return PeopleNo;
    }

    public void setPeopleNo(String peopleNo) {
        PeopleNo = peopleNo;
    }

    public String getStartingDate() {
        return StartingDate;
    }

    public void setStartingDate(String startingDate) {
        StartingDate = startingDate;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }
}
