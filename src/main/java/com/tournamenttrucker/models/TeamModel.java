package com.tournamenttrucker.models;

public class TeamModel {
    private int id;
    private String teamName;


    public TeamModel() {
    }
    public TeamModel(int id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public TeamModel(int id) {
        this.id = id;
    }

    public TeamModel(String teamName) {
        this.teamName = teamName;
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

}
