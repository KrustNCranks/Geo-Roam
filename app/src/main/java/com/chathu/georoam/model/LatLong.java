package com.chathu.georoam.model;

public class LatLong {
    private Double latitude;
    private Double longitude;

    public LatLong() {}

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

    public LatLong(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
