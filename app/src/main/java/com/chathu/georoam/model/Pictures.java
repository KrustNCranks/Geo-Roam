package com.chathu.georoam.model;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

public class Pictures {
    private String pictureName;
    private String pictureDescription;
    private String locationName;
    private String locationAddress;
    private Double latitude;
    private Double longitude;
    private String pictureURL;
    private String userID;
    private String isPrivate;
    private String mKey;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Pictures(String pictureName, String pictureDescription, String locationName, String locationAddress, Double latitude, Double longitude, String pictureURL, String userID, String isPrivate) {
        this.pictureName = pictureName;
        this.pictureDescription = pictureDescription;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureURL = pictureURL;
        this.userID = userID;
        this.isPrivate = isPrivate;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
