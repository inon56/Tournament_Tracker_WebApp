package com.tournamenttrucker.models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class TournamentModel {
    private int id;
    private String tournamentName;
    private double entryFee;
    private List<TeamModel> enteredTeams;
    private List<PrizeModel> prizes;
    private List<List<MatchupModel>> rounds;

    public TournamentModel() {
    }

    public TournamentModel(String tournamentName, double entryFee, List<TeamModel> enteredTeams, List<PrizeModel> prizes) {
        this.tournamentName = tournamentName;
        this.entryFee = entryFee;
        this.enteredTeams = enteredTeams;
        this.prizes = prizes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public double getEntryFee() {
        return entryFee;
    }


    public List<TeamModel> getEnteredTeams() {
        return enteredTeams;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public void setEnteredTeams(List<TeamModel> enteredTeams) {
        this.enteredTeams = enteredTeams;
    }

    public void setPrizes(List<PrizeModel> prizes) {
        this.prizes = prizes;
    }

    public void setRounds(List<List<MatchupModel>> rounds) {
        this.rounds = rounds;
    }

    public List<PrizeModel> getPrizes() {
        return prizes;
    }


    public List<List<MatchupModel>> getRounds() {
        return rounds;
    }

    @Override
    public String toString() {
        return "TournamentModel{" +
                "id=" + id +
                ", tournamentName='" + tournamentName + '\'' +
                ", entryFee=" + entryFee +
                ", enteredTeams=" + enteredTeams +
                ", prizes=" + prizes +
                ", rounds=" + rounds +
                '}';
    }
}
