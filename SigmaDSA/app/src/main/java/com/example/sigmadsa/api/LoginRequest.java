package com.example.sigmadsa.api;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("id")
    private final String username;
    @SerializedName("password")
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
