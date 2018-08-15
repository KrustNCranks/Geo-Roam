package com.chathu.georoam.model;

import com.google.android.gms.maps.model.LatLng;

public class EventsModel {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String eventLocationname;
    private String eventLocationAddress;
    private LatLng eventCoordinates;
    private String eventImageURL;
    private String userID;

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

    public LatLng getEventCoordinates() {
        return eventCoordinates;
    }

    public void setEventCoordinates(LatLng eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
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

    public EventsModel(String name, String description, String startDate, String endDate, String eventLocationname, String eventLocationAddress, LatLng eventCoordinates, String eventImageURL, String userID) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventLocationname = eventLocationname;
        this.eventLocationAddress = eventLocationAddress;
        this.eventCoordinates = eventCoordinates;
        this.eventImageURL = eventImageURL;
        this.userID = userID;
    }
}
