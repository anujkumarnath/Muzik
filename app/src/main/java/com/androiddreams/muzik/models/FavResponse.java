package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class FavResponse {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
