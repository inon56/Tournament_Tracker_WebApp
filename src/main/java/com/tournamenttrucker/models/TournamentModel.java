package com.tournamenttrucker.models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class TournamentModel {

    private transient int id;
    private String tournamentName;
    private double entryFee; // decimal

    private List<TeamModel> enteredTeams;

//    @Expose(serialize = true, deserialize = false)
    private List<PrizeModel> prizes;
    private transient List<List<MatchupModel>> rounds;

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
