package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class BookingViewModel {

    private String userName;

    private String SecondaryUserName;

    private float startTime;

    private float endTime;

    private String roomname;

    private String date;

    public BookingViewModel(String userName, String secondaryUserName, float startTime, float endTime, String roomname, String date) {
        this.userName = userName;
        SecondaryUserName = secondaryUserName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomname = roomname;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSecondaryUserName() {
        return SecondaryUserName;
    }

    public void setSecondaryUserName(String secondaryUserName) {
        SecondaryUserName = secondaryUserName;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
