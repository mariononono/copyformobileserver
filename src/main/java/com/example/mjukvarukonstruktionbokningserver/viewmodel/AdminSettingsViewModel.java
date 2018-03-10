package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class AdminSettingsViewModel {

    private int maxhours;

    public AdminSettingsViewModel(int maxhours) {
        this.maxhours = maxhours;
    }

    public int getMaxhours() {
        return maxhours;
    }

    public void setMaxhours(int maxhours) {
        this.maxhours = maxhours;
    }
}
