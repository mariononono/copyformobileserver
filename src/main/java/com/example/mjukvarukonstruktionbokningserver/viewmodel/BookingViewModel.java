package com.example.mjukvarukonstruktionbokningserver.viewmodel;

import java.util.Date;

public class BookingViewModel {

    private String userName;

    private String SecondaryUserName;

    private float startTime;

    private float endTime;

    private String roomname;

    private boolean checkeded;

    private boolean secondaryChecked;

    private Date date;

    public BookingViewModel(String userName, String secondaryUserName, float startTime, float endTime, String roomname, boolean checkeded, boolean secondaryChecked, Date date) {
        this.userName = userName;
        SecondaryUserName = secondaryUserName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomname = roomname;
        this.checkeded = checkeded;
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

    public boolean isCheckeded() {
        return checkeded;
    }

    public void setCheckeded(boolean checkeded) {
        this.checkeded = checkeded;
    }

    public boolean isSecondaryChecked() {
        return secondaryChecked;
    }

    public void setSecondaryChecked(boolean secondaryChecked) {
        this.secondaryChecked = secondaryChecked;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
