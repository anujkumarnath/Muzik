package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("username")
    private String mArtist;
    @SerializedName("straem_url")
    private String mStreanURL;
    @SerializedName("artwork_url")
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
        return mStreanURL;
    }

    public void setmStreanURL(String mStreanURL) {
        this.mStreanURL = mStreanURL;
    }

    public String getmArtWorkURL() {
        return mArtWorkURL;
    }

    public void setmArtWorkURL(String mArtWorkURL) {
        this.mArtWorkURL = mArtWorkURL;
    }
}
