package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("song")
    private String mTitle;
    @SerializedName("artist")
    private String mArtist;
    @SerializedName("link")
    private String mStreamURL;
    @SerializedName("icon")
    private String mArtWorkURL;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getmStreanURL() {
        return mStreamURL;
    }

    public void setmStreamURL(String mStreamURL) {
        this.mStreamURL = mStreamURL;
    }

    public String getmArtWorkURL() {
        return mArtWorkURL;
    }

    public void setmArtWorkURL(String mArtWorkURL) {
        this.mArtWorkURL = mArtWorkURL;
    }
}
