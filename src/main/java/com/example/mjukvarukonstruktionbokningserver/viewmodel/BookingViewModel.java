package com.example.mjukvarukonstruktionbokningserver.viewmodel;

import java.util.Date;

public class BookingViewModel {

    private String userName;

    private String SecondaryUserName;

    private float startTime;

    private float endTime;

    private String roomname;

    private boolean checked;

    private boolean secondaryChecked;

    private String date;

    public BookingViewModel(String userName, String secondaryUserName, float startTime, float endTime, String roomname, boolean checked, boolean secondaryChecked, String date) {
        this.userName = userName;
        SecondaryUserName = secondaryUserName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomname = roomname;
        this.checked = checked;
        this.secondaryChecked = secondaryChecked;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSecondaryChecked() {
        return secondaryChecked;
    }

    public void setSecondaryChecked(boolean secondaryChecked) {
        this.secondaryChecked = secondaryChecked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
