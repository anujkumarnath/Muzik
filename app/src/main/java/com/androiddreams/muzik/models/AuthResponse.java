package com.androiddreams.muzik.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
