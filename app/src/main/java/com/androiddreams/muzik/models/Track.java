package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("_id")
    private String id;
    @SerializedName("song")
    private String mTitle;
    @SerializedName("artist")
    private String mArtist;
    @SerializedName("link")
    private String mStreamURL;
    @SerializedName("icon")
    private String mArtWorkURL;
    @SerializedName("isFavourite")
    private boolean isFavourite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getmStreamURL() {
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

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
