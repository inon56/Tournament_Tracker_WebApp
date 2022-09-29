package com.tournamenttrucker.contracts;

import com.tournamenttrucker.PrizeGenerator;
import com.tournamenttrucker.PrizePercentageDistribution;

import java.util.List;
import java.util.Map;

public class TournamentResponse {
    private List<String> teamsNames;
    private Map<Integer, PrizePercentageDistribution> prizeOptions;


    public TournamentResponse(List<String> teams) {
        this.teamsNames = teams;
        prizeOptions = PrizeGenerator.Generate();
    }

    public Map<Integer, PrizePercentageDistribution> getPrizeOptions() {

        return prizeOptions;
    }
}
