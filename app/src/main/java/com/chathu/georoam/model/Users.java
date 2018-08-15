package com.chathu.georoam.model;

public class Users {
    private String personName;
    private String email;
    private String username;
    private String password;
    private String address;
    private String occupation;
    private String profilePictureName;
    private String profilePictureURL;

    public Users(){

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public Users(String personName, String email, String username, String password, String address, String occupation, String profilePictureName, String profilePictureURL) {
        this.personName = personName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.occupation = occupation;
        this.profilePictureName = profilePictureName;
        this.profilePictureURL = profilePictureURL;
    }
}
