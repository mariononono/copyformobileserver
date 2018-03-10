package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class UserViewModel {

    private String username;

    private String affiliation;

    private boolean isAdmin;

    private int firsthours;

    private int secondhours;

    private int thirdhours;

    public UserViewModel(String username, String affiliation, boolean isAdmin, int firsthours, int secondhours, int thirdhours) {
        this.username = username;
        this.affiliation = affiliation;
        this.isAdmin = isAdmin;
        this.firsthours = firsthours;
        this.secondhours = secondhours;
        this.thirdhours = thirdhours;
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
