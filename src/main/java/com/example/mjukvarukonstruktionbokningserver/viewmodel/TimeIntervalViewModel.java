package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class TimeIntervalViewModel {

    private float startTime;

    private float stopTime;

    public TimeIntervalViewModel(float startTime, float stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getStopTime() {
        return stopTime;
    }

    public void setStopTime(float stopTime) {
        this.stopTime = stopTime;
    }
}
