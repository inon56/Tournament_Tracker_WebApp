package com.tournamenttrucker.contracts;

import java.util.ArrayList;
import java.util.List;

public class CreateTournamentResponse {
    private List<String> teams;

    public CreateTournamentResponse(List<String> teams) {
        this.teams = teams;
    }

    public List<String> getTeams() {
        return teams;
    }

}
