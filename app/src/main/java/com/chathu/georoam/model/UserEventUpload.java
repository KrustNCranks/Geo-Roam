package com.chathu.georoam.model;

public class UserEventUpload {
    private String imageUrl;

    public UserEventUpload(){
        // Empty Constructor needed
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserEventUpload(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
