package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class CardItem {

    @SerializedName("title")
    private String title;

    @SerializedName("poster")
    private String imageURL;

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
