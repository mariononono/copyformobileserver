package com.example.mjukvarukonstruktionbokningserver.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
    private boolean checked;

    @Column(name = "secondary_checkin")
    private boolean secondaryChecked;

    @Column(name = "starttime", updatable = false)
    private float startTime;

    @Column(name = "endtime", updatable = false)
    private float endTime;

    @Column(name = "rooms", nullable = false)
    private String roomname;

    @Column(name = "date", nullable = false)
    private Date date;

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public boolean isSecondaryChecked() {
        return secondaryChecked;
    }

    public void setSecondaryChecked(boolean secondaryChecked) {
        this.secondaryChecked = secondaryChecked;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
