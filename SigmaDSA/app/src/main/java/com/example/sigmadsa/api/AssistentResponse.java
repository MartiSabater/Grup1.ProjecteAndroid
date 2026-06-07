package com.example.sigmadsa.api;

public class AssistentResponse {
    private String answer;
    private String response;
    private String message;

    public String getAnswer() {
        if (answer != null && !answer.trim().isEmpty()) {
            return answer;
        }
        if (response != null && !response.trim().isEmpty()) {
            return response;
        }
        return message;
    }
}
