package com.tournamenttrucker.contracts;

import com.tournamenttrucker.models.PersonModel;

import java.util.List;

public class CreateTeamRequest {
    private String teamName;
    private List<String> teamMembers; // emails

    public String getTeamName() {
        return teamName;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }
}
