package com.example.sigmadsa.api;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("id")
    private final String username;
    @SerializedName("email")
    private final String email;
    @SerializedName("nombre")
    private final String name;
    @SerializedName("password")
    private final String password;

    @SerializedName("avatar")
    private final String avatar;

    public RegisterRequest(String username, String email, String name, String password, String avatar) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }
}
