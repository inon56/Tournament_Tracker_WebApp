package com.tournamenttrucker.contracts;

public class MatchupResult {
    private String teamOneName;
    private String teamTwoName;
    private int teamOneScore;
    private int teamTwoScore;

    public MatchupResult() {
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public MatchupResult(String teamOneName, String teamTwoName, int teamOneScore, int teamTwoScore) {
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
    }

}
