package com.tournamenttrucker.contracts;

import com.tournamenttrucker.models.PersonModel;
import java.util.List;

public class CreateTeamRequest {
    private String teamName;
    private List<String> teamMembersEmails; // emails


    public String getTeamName() {
        return teamName;
    }

    public CreateTeamRequest(String teamName, List<String> teamMembersEmails) {
        this.teamName = teamName;
        this.teamMembersEmails = teamMembersEmails;
    }

    public List<String> getTeamMembers() {
        return teamMembersEmails;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamMembersEmails(List<String> teamMembersEmails) {
        this.teamMembersEmails = teamMembersEmails;
    }

    @Override
    public String toString() {
        return "{" +
                "teamName='" + teamName + '\'' +
                ", teamMembersEmails=" + teamMembersEmails +
                '}';
    }
}
