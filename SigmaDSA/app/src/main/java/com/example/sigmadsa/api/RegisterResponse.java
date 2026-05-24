package com.example.sigmadsa.api;

public class RegisterResponse {
    private boolean success;
    private String message;
    private String id;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getId() { return id; }
}
