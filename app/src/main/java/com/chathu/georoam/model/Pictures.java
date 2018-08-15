package com.chathu.georoam.model;



public class Pictures {
    private String pictureName;
    private String pictureDescription;
    private String locationName;
    private String locationAddress;
    private LatLong locationCoordinates;
    private String pictureURL;
    private String userID;

    public Pictures(){}

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public LatLong getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(LatLong locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Pictures(String pictureName, String pictureDescription, String locationName, String locationAddress, LatLong locationCoordinates, String pictureURL, String userID) {
        this.pictureName = pictureName;
        this.pictureDescription = pictureDescription;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationCoordinates = locationCoordinates;
        this.pictureURL = pictureURL;
        this.userID = userID;
    }
}
