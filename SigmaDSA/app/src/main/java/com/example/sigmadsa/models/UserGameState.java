package com.example.sigmadsa.models;

public class UserGameState {
    private int health;
    private int maxHealth;
    private int currentMissionId;
    private int currentObjectiveId;
    private String currentMissionTitle;
    private String currentObjectiveTitle;

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentMissionId() {
        return currentMissionId;
    }

    public int getCurrentObjectiveId() {
        return currentObjectiveId;
    }

    public String getCurrentMissionTitle() {
        return currentMissionTitle;
    }

    public String getCurrentObjectiveTitle() {
        return currentObjectiveTitle;
    }
}
