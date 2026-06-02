package com.example.sigmadsa.api;

public class AssistentRequest {
    private String question;//pregunta del usuari
    public AssistentRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
