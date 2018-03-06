package com.example.mjukvarukonstruktionbokningserver.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "booking")
@EntityListeners(AuditingEntityListener.class)
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String userName;

    @Column(name = "secondary_username")
    private String secondaryUserName;

    @Column(name = "checkedin")
    private boolean checkedIn;

    @Column(name = "secondary_checkin")
    private boolean secondaryCheckIn;

    @Column(name = "starttime", updatable = false)
    private float startTime;

    @Column(name = "endtime", updatable = false)
    private float endTime;

    @Column(name = "rooms", nullable = false)
    private String roomname;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "confirmation")
    private boolean confirmation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }


    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public boolean isSecondaryCheckIn() {
        return secondaryCheckIn;
    }

    public void setSecondaryCheckIn(boolean secondaryCheckIn) {
        this.secondaryCheckIn = secondaryCheckIn;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public String getSecondaryUserName() {
        return secondaryUserName;
    }

    public void setSecondaryUserName(String secondaryUserName) {
        this.secondaryUserName = secondaryUserName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }
}
