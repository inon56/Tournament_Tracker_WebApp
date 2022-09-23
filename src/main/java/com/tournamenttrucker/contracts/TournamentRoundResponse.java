package com.tournamenttrucker.contracts;

import java.util.List;

public class TournamentRoundResponse {
    private String tournamentName;
    private int round;
    private List<MatchupTeamsCompeting> matchups;

    public TournamentRoundResponse(String tournamentName, int round, List<MatchupTeamsCompeting> matchups) {
        this.tournamentName = tournamentName;
        this.round = round;
        this.matchups = matchups;
    }
}
