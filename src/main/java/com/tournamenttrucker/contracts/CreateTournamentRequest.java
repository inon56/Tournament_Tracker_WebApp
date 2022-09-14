package com.tournamenttrucker.contracts;


import java.util.List;

//String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"prizes\": [first,second]}";
public class CreateTournamentRequest {
    private String tournamentName;
    private int entryFee;
    private List<String> enteredTeams;
    private List<String> prizes;

    public String getTournamentName() {
        return tournamentName;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public List<String> getEnteredTeams() {
        return enteredTeams;
    }

    public List<String> getPrizes() {
        return prizes;
    }
}

