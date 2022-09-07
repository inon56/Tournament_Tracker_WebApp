package com.tournamenttrucker;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Gson gson = new Gson();
//        TournamentModel tournament;
//        String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"prizes\": [first,second]}";
//        System.out.println(jsonString);
//        tournament = gson.fromJson(jsonString, TournamentModel.class);
//        System.out.println(tournament);

        List<PersonModel> playersList = SQLConnector.getPerson_All();
        Gson gson = new Gson();
        String jsonString2 = gson.toJson(playersList);
        System.out.println(jsonString2);

//        Type listType = new TypeToken<List<PersonModel>>() {}.getType();
//        String jsonString = gson.toJson(playersList, listType);
//        SQLConnector.printPerson_All();

//        EmailLogic.sendEmail();

    }
}
