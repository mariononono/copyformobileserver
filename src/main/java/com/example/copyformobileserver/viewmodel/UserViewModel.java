package com.example.copyformobileserver.viewmodel;

public class UserViewModel {

    private String username;

    private String password;

    private String publickey;

    public UserViewModel(String username, String password, String publickey) {
        this.username = username;
        this.password = password;
        this.publickey = publickey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }
}
