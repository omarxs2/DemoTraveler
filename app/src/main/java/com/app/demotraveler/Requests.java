package com.app.demotraveler;

import java.util.HashMap;

public class Requests {


    private String EndingDate,PeopleNo,StartingDate,GuideID,Notes,RequestStatus;




    public Requests(){

    }


    public Requests(String endingDAte, String peopleNo, String startingDate, String guideID, String notes, String requestStatus) {
        EndingDate = endingDAte;
        PeopleNo = peopleNo;
        StartingDate = startingDate;
        GuideID = guideID;
        Notes = notes;
        RequestStatus = requestStatus;
    }

    public String getEndingDAte() {
        return EndingDate;
    }

    public void setEndingDAte(String endingDAte) {
        EndingDate = endingDAte;
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

    public String getGuideID() {
        return GuideID;
    }

    public void setGuideID(String guideID) {
        GuideID = guideID;
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
