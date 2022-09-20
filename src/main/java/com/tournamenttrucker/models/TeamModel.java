package com.tournamenttrucker.models;

import java.util.ArrayList;
import java.util.List;

public class TeamModel {
    private int id;
    private String teamName;
    private List<PersonModel> teamMembers;
    public TeamModel() {
    }

    public TeamModel(String teamName, List<PersonModel> teamMembers) {
        this.teamName = teamName;
        this.teamMembers =  new ArrayList<>(teamMembers);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public List<PersonModel> getTeamMembers() {
        return teamMembers;
    }

    @Override
    public String toString() {
        return "TeamModel{" +
                "teamName='" + teamName + '\'' +
                ", teamMembers=" + teamMembers +
                '}';
    }
}
