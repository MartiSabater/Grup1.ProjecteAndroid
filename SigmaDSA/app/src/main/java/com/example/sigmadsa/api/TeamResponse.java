package com.example.sigmadsa.api;

import java.util.List;
public class TeamResponse {
    private String team;
    private List<TeamMemberResponse> members;

    public String getTeam() {
        return team;
    }

    public List<TeamMemberResponse> getMembers() {
        return members;
    }
}
