package com.tournamenttrucker.contracts;

import java.util.ArrayList;
import java.util.List;

public class CreateTournamentResponse {
    private List<String> teams;
    private List<String> prizes;

    public CreateTournamentResponse(List<String> teams, List<String> prizes) {
        this.teams = teams;
        this.prizes = prizes;
    }

    @Override
    public String toString() {
        return "CreateTournamentResponse{" +
                "teams=" + teams +
                ", prizes=" + prizes +
                '}';
    }

    public List<String> getTeams() {
        return teams;
    }

    public List<String> getPrizes() {
        return prizes;
    }
}
