package com.tournamenttrucker.contracts;

import java.util.List;

public class SubmitRoundResultRequest {
    private String tournamentName;
    private int matchupRound;
    private List<MatchupResult> matchupResults;


    public List<MatchupResult> getMatchupResults() {
        return matchupResults;
    }

    public int getMatchupRound() {
        return matchupRound;
    }

    public String getTournamentName() {
        return tournamentName;
    }
}
