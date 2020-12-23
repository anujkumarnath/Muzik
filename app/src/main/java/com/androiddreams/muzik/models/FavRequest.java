package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class FavRequest {
    @SerializedName("id")
    private String trackId;
    @SerializedName("email")
    private String username;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
