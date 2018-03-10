package com.example.mjukvarukonstruktionbokningserver.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int Id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "affiliation")
    private String affiliation;

    @Column(name = "isadmin")
    private boolean isAdmin;

    @Column(name = "currentweek")
    private int currentweek;

    @Column(name = "firstweek")
    private int firsthours;

    @Column(name = "secondweek")
    private int secondhours;

    @Column(name = "thirdweek")
    private int thirdhours;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getCurrentweek() {
        return currentweek;
    }

    public void setCurrentweek(int currentweek) {
        this.currentweek = currentweek;
    }

    public int getFirsthours() {
        return firsthours;
    }

    public void setFirsthours(int firsthours) {
        this.firsthours = firsthours;
    }

    public int getSecondhours() {
        return secondhours;
    }

    public void setSecondhours(int secondhours) {
        this.secondhours = secondhours;
    }

    public int getThirdhours() {
        return thirdhours;
    }

    public void setThirdhours(int thirdhours) {
        this.thirdhours = thirdhours;
    }

}
