package com.example.fitnesshelper.models;

public class User {

    private String name,email,password, profileImgLink;

    public User(String name, String email, String password, String profileImgLink) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImgLink = profileImgLink;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImgLink() {
        return profileImgLink;
    }

    public void setProfileImgLink(String profileImgLink) {
        this.profileImgLink = profileImgLink;
    }
}
