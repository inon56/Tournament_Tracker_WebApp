package com.tournamenttrucker.contracts;


import java.util.List;

//String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"prizes\": [first,second]}";
public class CreateTournamentRequest {
    public String tournamentName;
    public int entryFee;
    public List<String> enteredTeams;
    public List<String> prizes;
}

