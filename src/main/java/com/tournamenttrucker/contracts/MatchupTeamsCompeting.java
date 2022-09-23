package com.tournamenttrucker.contracts;

public class MatchupTeamsCompeting {
    private String teamOneName;
    private String teamTwoName;


    public MatchupTeamsCompeting(String teamOneName, String teamTwoName) {
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }
}
