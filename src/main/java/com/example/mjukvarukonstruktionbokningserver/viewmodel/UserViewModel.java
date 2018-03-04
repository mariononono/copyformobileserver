package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class UserViewModel {

    private String username;

    private String affiliation;

    private boolean isAdmin;

    private String hours;

    public UserViewModel(String username, String affiliation, boolean isAdmin, String hours) {
        this.username = username;
        this.affiliation = affiliation;
        this.isAdmin = isAdmin;
        this.hours = hours;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
