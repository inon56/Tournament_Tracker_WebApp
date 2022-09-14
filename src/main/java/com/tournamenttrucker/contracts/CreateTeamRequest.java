package com.tournamenttrucker.contracts;

import com.tournamenttrucker.models.PersonModel;
import java.util.List;

public class CreateTeamRequest {
    private String teamName;
    private List<String> teamMembersEmails; // emails

    public String getTeamName() {
        return teamName;
    }

    public List<String> getTeamMembers() {
        return teamMembersEmails;
    }

    @Override
    public String toString() {
        return "{" +
                "teamName='" + teamName + '\'' +
                ", teamMembersEmails=" + teamMembersEmails +
                '}';
    }
}
