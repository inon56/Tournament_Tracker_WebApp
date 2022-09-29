package com.tournamenttrucker.contracts;

import com.tournamenttrucker.models.PersonModel;
import java.util.List;

public class CreateTeamRequest {
    private String teamName;
    private List<String> teamMembersEmails; // emails

    public CreateTeamRequest(String teamName, List<String> teamMembersEmails) {
        this.teamName = teamName;
        this.teamMembersEmails = teamMembersEmails;
    }

    public String getTeamName() {
        return teamName;
    }

    public List<String> getTeamMembersEmails() {
        return teamMembersEmails;
    }
}
