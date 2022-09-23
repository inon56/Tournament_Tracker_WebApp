package com.tournamenttrucker.models;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// Represents one match in the tournament
/// </summary>
public class MatchupModel {

    // The unique identifier for the matchup
    private int id;
    /// Which round this match is a part of
    private int matchupRound;
    /// The ID from the database that will be used to identify the winner
    private int winnerId;
    private int tournamentId;
    private int teamOneId;
    private int teamTwoId;
    private int teamOneScore;
    private int teamTwoScore;


    public int getTournamentId() {
        return tournamentId;
    }


    public int getTeamOneScore() {
        return teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public int getId() {
        return id;
    }


    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public void setTeamOneId(int teamOneId) {
        this.teamOneId = teamOneId;
    }

    public void setTeamTwoId(int teamTwoId) {
        this.teamTwoId = teamTwoId;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public int getTeamOneId() {
        return teamOneId;
    }

    public int getTeamTwoId() {
        return teamTwoId;
    }

    public int getMatchupRound() {
        return matchupRound;
    }

    public void setMatchupRound(int matchupRound) {
        this.matchupRound = matchupRound;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }
}
