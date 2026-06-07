package com.example.sigmadsa.models;

import java.util.List;

public class Mission {
    private int id;
    private int missionOrder;
    private String title;
    private String description;
    private List<Objective> objectives;

    public int getId() {
        return id != 0 ? id : missionOrder;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }
}
