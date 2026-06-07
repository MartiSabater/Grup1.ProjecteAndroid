package com.example.sigmadsa.api;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("id")
    private final String username;
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;

    public LoginRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
