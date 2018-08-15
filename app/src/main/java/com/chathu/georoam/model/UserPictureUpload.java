package com.chathu.georoam.model;

public class UserPictureUpload {
    private String imageUrl;

    public UserPictureUpload(){

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserPictureUpload(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
