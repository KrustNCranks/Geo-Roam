package com.chathu.georoam.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

public class EventsModel {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String eventLocationname;
    private String eventLocationAddress;
    private Double latitude;
    private Double longitude;
    private String eventImageURL;
    private String userID;
    private String isPrivate;
    private String mKey;

    public EventsModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventLocationname() {
        return eventLocationname;
    }

    public void setEventLocationname(String eventLocationname) {
        this.eventLocationname = eventLocationname;
    }

    public String getEventLocationAddress() {
        return eventLocationAddress;
    }

    public void setEventLocationAddress(String eventLocationAddress) {
        this.eventLocationAddress = eventLocationAddress;
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

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
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

    public EventsModel(String name, String description, String startDate, String endDate, String eventLocationname, String eventLocationAddress, Double latitude, Double longitude, String eventImageURL, String userID, String isPrivate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventLocationname = eventLocationname;
        this.eventLocationAddress = eventLocationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventImageURL = eventImageURL;
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
