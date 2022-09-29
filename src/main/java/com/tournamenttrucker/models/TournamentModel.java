package com.tournamenttrucker.models;

import com.tournamenttrucker.PrizePercentageDistribution;

public class TournamentModel {
    private int id;
    private String tournamentName;
    private double entryFee;
    private int active;
    private int currentRound;
    private int prizeOption;
    private PrizePercentageDistribution prizePercentageDistribution;

    public TournamentModel() {
    }

    public TournamentModel(int id, String tournamentName, double entryFee, int active, int currentRound, int prizeOption, PrizePercentageDistribution prizePercentageDistribution) {
        this.id = id;
        this.tournamentName = tournamentName;
        this.entryFee = entryFee;
        this.active = active;
        this.currentRound = currentRound;
        this.prizeOption = prizeOption;
        this.prizePercentageDistribution = prizePercentageDistribution;
    }

    public int getPrizeOption() {
        return prizeOption;
    }

    public void setPrizeOption(int prizeOption) {
        this.prizeOption = prizeOption;
    }

    public int getActive() {
        return active;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public PrizePercentageDistribution getPrizePercentageDistribution() {
        return prizePercentageDistribution;
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


    public void setActive(int active) {
        this.active = active;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setPrizePercentageDistribution(PrizePercentageDistribution prizePercentageDistribution) {
        this.prizePercentageDistribution = prizePercentageDistribution;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }




    @Override
    public String toString() {
        return "TournamentModel{" +
                "id=" + id +
                ", tournamentName='" + tournamentName + '\'' +
                ", entryFee=" + entryFee +
                '}';
    }
}
