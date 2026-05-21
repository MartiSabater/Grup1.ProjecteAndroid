package com.example.sigmadsa.api;

public class RegisterRequest {
    private final String username;
    private final String email;
    private final String name;
    private final String password;

    public RegisterRequest(String username, String email, String name, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
