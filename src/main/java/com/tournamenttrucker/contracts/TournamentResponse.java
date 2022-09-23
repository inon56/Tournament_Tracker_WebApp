package com.tournamenttrucker.contracts;

import com.tournamenttrucker.models.PrizeGenerator;
import com.tournamenttrucker.models.PrizePercentageDistribution;

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

    public List<String> getTeamsNames() {
        return teamsNames;
    }

//    @Override
//    public String toString() {
//        return  "\"teams\": " + teamsNames +
//                "\n" +
//                "        \"prizeOptions\":\n" +
//                "            [\n" +
//                "        {\"option\": \"1\", \"firstPlace\": \"100%\"},\n" +
//                "        {\"option\": \"2\", \"firstPlace\": \"80%\", \"secondPlace\": \"20%\"},\n" +
//                "        {\"option\": \"3\", \"firstPlace\": \"70%\", \"secondPlace\": \"30%\"},\n" +
//                "        {\"option\": \"4\", \"firstPlace\": \"60%\", \"secondPlace\": \"40%\"},\n" +
//                "        {\"option\": \"5\", \"firstPlace\": \"60%\", \"secondPlace\": \"30%\", \"thirdPlace\": \"10%\"}\n" +
//                "            ]";

//        StringBuilder prizeOptions = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }

//        for (Map.Entry<Integer, PrizePercentageDistribution> entry : prizes.entrySet())
//        {
//            prizeOptions = entry.getValue().getFirst() + " ";
//            entry.getValue().getSecond();
//            entry.getValue().getThird();
//        }
//    }
}
