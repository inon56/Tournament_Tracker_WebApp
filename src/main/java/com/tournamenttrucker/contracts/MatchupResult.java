package com.tournamenttrucker.contracts;

public class MatchupResult {
    private String teamOneName;
    private String teamTwoName;
    private int teamOneScore;
    private int teamTwoScore;

    public MatchupResult() {
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

    @Override
    public String toString() {
        return "MatchupResult{" +
                "teamOneName='" + teamOneName + '\'' +
                ", teamTwoName='" + teamTwoName + '\'' +
                ", teamOneScore=" + teamOneScore +
                ", teamTwoScore=" + teamTwoScore +
                '}';
    }
}
