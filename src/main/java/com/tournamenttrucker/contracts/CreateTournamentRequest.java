package com.tournamenttrucker.contracts;


import java.util.List;

//String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"prizes\": [first,second]}";
public class CreateTournamentRequest {
    private String tournamentName;
    private int entryFee;
    private List<String> enteredTeams;
    private int prizeOption;


    public String getTournamentName() {
        return tournamentName;
    }
    public int getEntryFee() {
        return entryFee;
    }
    public List<String> getEnteredTeams() {
        return enteredTeams;
    }

    public int getPrizeOption() {
        return prizeOption;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public void setEnteredTeams(List<String> enteredTeams) {
        this.enteredTeams = enteredTeams;
    }

    public void setPrizeOption(int prizeOption) {
        this.prizeOption = prizeOption;
    }

    @Override
    public String toString() {
        return "CreateTournamentRequest{" +
                "tournamentName='" + tournamentName + '\'' +
                ", entryFee=" + entryFee +
                ", enteredTeams=" + enteredTeams +
                '}';
    }
}

