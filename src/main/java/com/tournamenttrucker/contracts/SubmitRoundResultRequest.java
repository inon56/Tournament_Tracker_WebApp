package com.tournamenttrucker.contracts;

import java.util.List;

public class SubmitRoundResultRequest {
    private String tournamentName;
    private int round;
    private List<MatchupResult> matchupsResults;

    public SubmitRoundResultRequest(String tournamentName, int matchupRound, List<MatchupResult> matchupResults) {
        this.tournamentName = tournamentName;
        this.round = matchupRound;
        this.matchupsResults = matchupResults;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setMatchupsResults(List<MatchupResult> matchupsResults) {
        this.matchupsResults = matchupsResults;
    }

    public List<MatchupResult> getMatchupsResults() {
        return matchupsResults;
    }

    public int getRound() {
        return round;
    }

    public String getTournamentName() {
        return tournamentName;
    }

}
